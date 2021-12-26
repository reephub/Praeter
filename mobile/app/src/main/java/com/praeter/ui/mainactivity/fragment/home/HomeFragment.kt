package com.praeter.ui.mainactivity.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.*
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.maps.android.PolyUtil
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.praeter.R
import com.praeter.core.utils.PraeterLocationManager
import com.praeter.core.utils.UIManager
import com.praeter.data.local.bean.MapsEnum
import com.praeter.data.remote.dto.directions.Steps
import com.praeter.databinding.FragmentHomeBinding
import com.praeter.ui.maps.BottomSheetLocationFragment
import com.praeter.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException
import java.text.DateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class HomeFragment : Fragment(),
    CoroutineScope,
    LocationListener,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mHomeViewModel: HomeViewModel by viewModels()


    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private lateinit var mapFragment: SupportMapFragment
    private var geocoder: Geocoder? = null
    private lateinit var mMap: GoogleMap
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mCriteria: Criteria? = null
    private var mProvider: String? = ""

    // bunch of location related apis
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    // boolean flag to toggle the ui
    private var mRequestingLocationUpdates: Boolean? = null

    // location last updated time
    private var mLastUpdateTime: String? = null

    private var CURRENT_LOCATION_TO_STRING: String = ""
    private var TARGET_LOCATION_TO_STRING: String = ""


    /////////////////////////////////////
    //
    // INSTANCE
    //
    /////////////////////////////////////
    fun newInstance(): HomeFragment {
        return HomeFragment()
    }

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.i("onCreateView()")
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated()")

        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        Timber.i("All permissions are granted")
                        initAutoCompleteView()
                        mRequestingLocationUpdates = true
                        initGoogleMap()
                        initLocationSettings()
                    } else {
                        Timber.e("All permissions are not granted")
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest>,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            })
            .withErrorListener { dexterError: DexterError -> Timber.e(dexterError.toString()) }
            .onSameThread()
            .check()

        initViewModelsObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("onActivityResult()")
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == Constants.REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> Timber.e("User agreed to make required location settings changes.")
                Activity.RESULT_CANCELED -> {
                    Timber.e("User chose not to make required location settings changes.")
                    mRequestingLocationUpdates = false
                }
                else -> {
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
        if (mRequestingLocationUpdates!!) {
            // pausing location updates
            stopLocationUpdates()
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.e("onResume()")
        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates!!) {
            startLocationUpdates()
        }
        updateLocationUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _viewBinding = null
    }

    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    @SuppressLint("MissingPermission")
    override fun onLocationChanged(location: Location) {
        Timber.i("onLocationChanged() - Lat :${location.latitude} - Lon : ${location.longitude}")

        // Remove markers
        mMap.clear()

        val latitude = location.latitude
        val longitude = location.longitude

        val latLng = LatLng(latitude, longitude)

        /*mMap.addMarker {
            position(latLng)
            title(PraeterLocationManager.getDeviceLocationToString(geocoder, location))
        }*/

        mMap.isMyLocationEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapsEnum.BUILDINGS.distance))
        mMap.uiSettings.isScrollGesturesEnabled = true
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Timber.d("onStatusChanged()")
    }

    override fun onProviderEnabled(provider: String) {
        Timber.d("onProviderEnabled()")
    }

    override fun onProviderDisabled(provider: String) {
        Timber.d("onProviderDisabled()")
    }


    override fun onMyLocationButtonClick(): Boolean {
        Timber.d("onMyLocationButtonClick()")
        Toast.makeText(requireActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Timber.e("onMyLocationClick() - $location")
        // Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()

        BottomSheetLocationFragment
            .newInstance(location)
            .show(
                this.childFragmentManager,
                BottomSheetLocationFragment.TAG
            )
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    private fun initViewModelsObservers() {
        Timber.i("initViewModelsObservers()")
        mHomeViewModel.getGoogleDirection()
            .observe(
                requireActivity(),
                { response ->

                    try {

                        Timber.d("Overview : ${response.routes[0].overviewPolyline.points}")
                        val overview = response.routes[0].overviewPolyline.points


                        val stepsNumber = response.routes[0].legs[0].steps.size

                        val polylines: Array<String?> = arrayOfNulls(stepsNumber)

                        for ((i, step: Steps) in response.routes[0].legs[0].steps.withIndex()) {

                            val polygone: String = step.polyline.points

                            polylines[i] = polygone
                        }

                        Timber.e("Polylines : ${polylines.toString()}")
                        val polylinesCount = polylines.size
                        for (i in 0..polylinesCount) {

                            val option: PolylineOptions = PolylineOptions()
                            option.color(
                                ContextCompat.getColor(
                                    requireActivity(),
                                    R.color.black
                                )
                            )
                            option.addAll(PolyUtil.decode(overview))

                            mMap.addPolyline(option)
                        }

                        val southWestBounds = TARGET_LOCATION_TO_STRING.split(",")
                        val northEastBounds = CURRENT_LOCATION_TO_STRING.split(",")


                        val australiaBounds = LatLngBounds(
                            LatLng(
                                southWestBounds[0].toDouble(),
                                southWestBounds[1].toDouble()
                            ),  // SW bounds
                            LatLng(
                                northEastBounds[0].toDouble(),
                                northEastBounds[1].toDouble()
                            ) // NE bounds
                        )
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                australiaBounds.center,
                                10f
                            )
                        )

                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                })
    }

    private fun initLocationSettings() {
        Timber.i("initLocationSettings()")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mSettingsClient = LocationServices.getSettingsClient(requireContext())
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // location is received
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
                updateLocationUI()
            }
        }
        mRequestingLocationUpdates = false
        mLocationRequest = createLocationRequest()
        mLocationRequest!!.interval = Constants.UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()
        geocoder = Geocoder(context, Locale.getDefault())
    }

    private fun createLocationRequest(): LocationRequest {
        Timber.d("createLocationRequest()")
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private var placesClient: PlacesClient? = null
    private var currentPlace: Place? = null
    private var request: FindCurrentPlaceRequest? = null

    @SuppressLint("MissingPermission")
    private fun initAutoCompleteView() {
        Timber.i("initAutoCompleteView()")

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            this.childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // ref : https://stackoverflow.com/questions/36398061/how-to-change-text-size-in-places-autocompletefragment-in-android
        // placeAutocompleteFragment - is my PlaceAutocompleteFragment instance
        (autocompleteFragment.view
            ?.findViewById(R.id.places_autocomplete_search_input) as EditText)
            .setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(
                requireContext(),
                getString(R.string.google_maps_key),
                Locale.FRANCE
            )

            placesClient = Places.createClient(requireContext())

            // Use the builder to create a FindCurrentPlaceRequest.
            request = FindCurrentPlaceRequest.newInstance(Constants.CURRENT_PLACE_FIELDS)

            placesClient
                ?.findCurrentPlace(request!!)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val response = task.result
                        var previousPercentage: Double = 0.0

                        for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods
                            ?: emptyList()) {
                            Timber.e(
                                "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}"
                            )

                            if (previousPercentage < placeLikelihood.likelihood) {
                                previousPercentage = placeLikelihood.likelihood
                                currentPlace = placeLikelihood.place
                            }
                        }

                        Timber.d("final place : $currentPlace")
                    } else {
                        val exception = task.exception
                        if (exception is ApiException) {
                            Timber.e("Place not found: ${exception.statusCode}")
                        }
                    }
                }
        }

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Constants.PLACES_FIELDS)

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Timber.i("onPlaceSelected() - Place: ${place.name}, ${place.id}, ${place.latLng}")

                place.latLng?.let {
                    mMap.addMarker {
                        position(it)
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
                }

                showBottomItineraryFragment(currentPlace!!, place)
                buildItinerary(currentPlace!!, place)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Timber.e("An error occurred: $status")
            }
        })
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    private fun initGoogleMap() {
        Timber.i("setupMap()")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = this.childFragmentManager
            .findFragmentById(R.id.maps_container) as SupportMapFragment

        // Setup Google Map using coroutines
        lifecycle.coroutineScope.launchWhenCreated {
            Timber.d("Setup Google Map using coroutines")
            mMap = mapFragment.awaitMap()

            setupMap()

            // Set a preference for minimum and maximum zoom.
            mMap.setMinZoomPreference(MapsEnum.WORLD.distance)
            mMap.setMaxZoomPreference(MapsEnum.DEFAULT_MAX_ZOOM.distance)
            //mMap.moveCamera(CameraUpdateFactory.zoomTo(MapsEnum.WORLD.distance))

            CoroutineScope(coroutineContext).launch {
                delay(3000)
                setLocationSettings()
                hideLoading()
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        Timber.d("initGoogleMap()")

        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
    }

    private fun setLocationSettings() {
        Timber.i("setLocationSettings()")
        mLocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mCriteria = Criteria()
        mProvider = mLocationManager!!.getBestProvider(mCriteria!!, true)
        try {
            mLocation = mLocationManager!!.getLastKnownLocation(mProvider!!)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        if (mLocation != null) {
            onLocationChanged(mLocation!!)
        }
        try {
            mLocationManager!!.requestLocationUpdates(
                mProvider!!,
                20000, 0f,
                this
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun hideLoading() {
        if (binding.rlMapsLoading.visibility == View.VISIBLE)
            startAnimation(binding.rlMapsLoading)
    }

    private fun startAnimation(view: View) {
        Timber.d("startAnimation()")
        val animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        animFadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // Ignored
            }

            override fun onAnimationEnd(animation: Animation) {
                Timber.d("onAnimationEnd()")
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Ignored
            }
        })
        val mAnimationSet = AnimationSet(true)
        mAnimationSet.interpolator = AccelerateInterpolator()
        mAnimationSet.addAnimation(animFadeOut)
        view.startAnimation(mAnimationSet)
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mSettingsClient
            ?.checkLocationSettings(mLocationSettingsRequest!!)
            ?.addOnSuccessListener(requireActivity()) {
                Timber.i("All location settings are satisfied. Started location updates!")
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest!!,
                    mLocationCallback!!,
                    Looper.myLooper()!!
                )
                updateLocationUI()
            }
            ?.addOnFailureListener(requireActivity()) { e: Exception ->
                val statusCode = (e as ApiException).statusCode
                if (statusCode == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    Timber.i("Location settings are not satisfied. Attempting to upgrade location settings ")
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the
                        // result in onActivityResult().
                        val rae = e as ResolvableApiException
                        rae.startResolutionForResult(
                            requireActivity(),
                            Constants.REQUEST_CHECK_SETTINGS
                        )
                    } catch (sie: SendIntentException) {
                        Timber.i("PendingIntent unable to execute request.")
                    }
                } else if (statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    val errorMessage = "Location settings are inadequate, and cannot be " +
                            "fixed here. Fix in Settings."
                    Timber.e(errorMessage)
                    UIManager.showActionInToast(requireActivity(), errorMessage)
                }
                updateLocationUI()
            }
    }

    private fun stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
            ?.removeLocationUpdates(mLocationCallback!!)
            ?.addOnCompleteListener(requireActivity()) {
                UIManager.showActionInToast(requireActivity(), "Location updates stopped!")
            }
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private fun updateLocationUI() {
        if (null != mCurrentLocation) {
            Timber.e(
                "Lat: " + mCurrentLocation!!.latitude + ", " +
                        "Lng: " + mCurrentLocation!!.longitude
            )
            geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>
            try {
                addresses = geocoder!!.getFromLocation(
                    mCurrentLocation!!.latitude,
                    mCurrentLocation!!.longitude,  // In this sample, get just a single address.
                    1
                )
                Timber.e(addresses[0].countryName)
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            mCurrentLocation!!.latitude,
                            mCurrentLocation!!.longitude
                        ),
                        MapsEnum.BUILDINGS.distance
                    )
                )
            } catch (ioException: IOException) {
                // Catch network or other I/O problems.
//            errorMessage = getString(R.string.service_not_available);
                Timber.e(ioException, "errorMessage")
            } catch (illegalArgumentException: IllegalArgumentException) {
                // Catch invalid latitude or longitude values.
//            errorMessage = getString(R.string.invalid_lat_long_used);
                Timber.e(
                    illegalArgumentException,
                    "errorMessage. Latitude = ${mCurrentLocation!!.latitude}, Longitude = ${mCurrentLocation!!.longitude}"
                )
            }
        }
    }

    private fun showBottomItineraryFragment(currentPlace: Place, targetPlace: Place) {

        BottomSheetItineraryFragment
            .newInstance(currentPlace, targetPlace)
            .show(
                this.childFragmentManager,
                BottomSheetItineraryFragment.TAG
            )
    }

    private fun buildItinerary(currentPlace: Place, targetPlace: Place) {
        val currentLocation = Location("")
        currentLocation.latitude = currentPlace.latLng?.latitude!!
        currentLocation.longitude = currentPlace.latLng?.longitude!!

        val targetLocation = Location("")
        targetLocation.latitude = targetPlace.latLng?.latitude!!
        targetLocation.longitude = targetPlace.latLng?.longitude!!

        CURRENT_LOCATION_TO_STRING =
            PraeterLocationManager.convertLatLngLocationToString(currentLocation)
        TARGET_LOCATION_TO_STRING =
            PraeterLocationManager.convertLatLngLocationToString(targetLocation)

        mHomeViewModel.getItinerary(CURRENT_LOCATION_TO_STRING, TARGET_LOCATION_TO_STRING)
    }
}