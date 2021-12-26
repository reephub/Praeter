package com.praeter.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.coroutineScope
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
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
import com.praeter.data.remote.dto.directions.Steps
import com.praeter.databinding.ActivityLocationBinding
import com.praeter.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class LocationActivity : BaseActivity(),
    CoroutineScope,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    companion object {
        private const val CHRONOPOST_LOCATION: String =
            "48.819066,2.328843"
        private const val BELTOISE_RACE_TRACK_LOCATION: String =
            "48.757288,1.987732"

        private const val REQUESTING_LOCATION_UPDATES_KEY: String =
            "REQUESTING_LOCATION_UPDATES_KEY"

        private const val REQUEST_CHECK_SETTINGS: Int = 34563
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()


    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private var _viewBinding: ActivityLocationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mViewModel: LocationViewModel by viewModels()

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var requestingLocationUpdates: Boolean = false

    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateValuesFromBundle(savedInstanceState)

        _viewBinding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Dexter
            .withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(permissionsReport: MultiplePermissionsReport?) {
                    if (permissionsReport?.isAnyPermissionPermanentlyDenied == true) {
                        Timber.e("Permissions are denied")
                    }

                    if (permissionsReport?.areAllPermissionsGranted() == true) {
                        Timber.d("Permissions are granted")
                        initAutoCompleteView()
                        initMapsView()

                        launchCoroutine()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener { dexterError: DexterError -> Timber.e(dexterError.toString()) }
            .onSameThread()
            .check()

        initViewModelsObservers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.d("onSaveInstanceState()")
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            Timber.i("onActivityResult()")
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Timber.i("Activity.RESULT_OK - Place: ${place.name}, ${place.id}")

                        mGoogleMap.addMarker {
                            position(place.latLng!!)
                        }
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng!!))

/*
                        // Specify the fields to return.
                        val placeFields = listOf(Place.Field.ID, Place.Field.NAME)

                        // Construct a request object, passing the place ID and fields array.
                        val request = FetchPlaceRequest.newInstance(place.id!!, placeFields)

                        // Create a new Places client instance.
                        val placesClient = Places.createClient(this@LocationActivity)
                        placesClient.fetchPlace(request)
                            .addOnSuccessListener { response: FetchPlaceResponse ->
                                val place = response.place
                                Timber.i("Place found: ${place.name}")
                            }.addOnFailureListener { exception: Exception ->
                                if (exception is ApiException) {
                                    Timber.e("Place not found: ${exception.message}")
                                    val statusCode = exception.statusCode
                                    TODO("Handle error with given status code")
                                }
                            }*/
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Timber.e(status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }


    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        Timber.i("updateValuesFromBundle()")
        savedInstanceState ?: return

        // Update the value of requestingLocationUpdates from the Bundle.
        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            requestingLocationUpdates = savedInstanceState.getBoolean(
                REQUESTING_LOCATION_UPDATES_KEY
            )
        }

        // ...

        // Update UI to match restored state
        updateUI()
    }

    private fun initViewModelsObservers() {
        Timber.i("initViewModelsObservers()")
        mViewModel.getGoogleDirection()
            .observe(
                this,
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
                                    this@LocationActivity,
                                    R.color.black
                                )
                            )
                            option.addAll(PolyUtil.decode(overview))

                            mGoogleMap.addPolyline(option)
                        }


                        val southWestBounds = BELTOISE_RACE_TRACK_LOCATION.split(",")
                        val northEastBounds = CHRONOPOST_LOCATION.split(",")


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
                        mGoogleMap.moveCamera(
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

    private fun initAutoCompleteView() {
        Timber.i("initAutoCompleteView()")

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // ref : https://stackoverflow.com/questions/36398061/how-to-change-text-size-in-places-autocompletefragment-in-android
        // placeAutocompleteFragment - is my PlaceAutocompleteFragment instance
        (autocompleteFragment.view
            ?.findViewById(R.id.places_autocomplete_search_input) as EditText)
            .setTextColor(ContextCompat.getColor(this, R.color.white))

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(
                applicationContext,
                getString(R.string.google_maps_key),
                Locale.FRANCE
            );
        }

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.BUSINESS_STATUS,
                Place.Field.ID,
                Place.Field.LAT_LNG,
                Place.Field.NAME,
                Place.Field.OPENING_HOURS,
                Place.Field.PHONE_NUMBER,
                Place.Field.TYPES,
                Place.Field.VIEWPORT,
                Place.Field.UTC_OFFSET
            )
        )

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Timber.i("onPlaceSelected() - Place: ${place.name}, ${place.id}, ${place.latLng}")

                place.latLng?.let {
                    mGoogleMap.addMarker {
                        position(it)
                    }
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
                }

            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Timber.e("An error occurred: $status")
            }
        })
    }

    private fun initMapsView() {
        Timber.i("initMapsView()")

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps_container) as SupportMapFragment

        // Setup Google Map using listener implementation
        // mapFragment.getMapAsync(this@LocationActivity)

        // Setup Google Map using coroutines
        lifecycle.coroutineScope.launchWhenCreated {
            Timber.d("Setup Google Map using coroutines")
            mGoogleMap = mapFragment.awaitMap()

            initGoogleMap()
        }

        initLocationClient()

    }

    @SuppressLint("MissingPermission")
    private fun initLocationClient() {
        Timber.i("initLocationClient()")

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@LocationActivity)

        fusedLocationClient
            ?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                Timber.d("Got last known location : $location")
            }

        locationRequest = createLocationRequest()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this@LocationActivity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { _ ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        this@LocationActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...

                    Timber.d("LocationCallback() - onLocationResult() - location : $location")

                    currentLocation = location
                }
            }
        }
    }

    private fun createLocationRequest(): LocationRequest {
        Timber.d("createLocationRequest()")
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun launchPlaceIntent() {
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ID, Place.Field.NAME)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        Timber.d("startLocationUpdates()")
        fusedLocationClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    /**
     * Consider whether you want to stop the location updates when the activity is no longer in focus,
     * such as when the user switches to another app or to a different activity in the same app.
     * This can be handy to reduce power consumption, provided the app doesn't need to collect information even when it's running in the background.
     * This section shows how you can stop the updates in the activity's onPause() method.
     */
    private fun stopLocationUpdates() {
        Timber.e("stopLocationUpdates()")
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun initGoogleMap() {
        Timber.d("initGoogleMap()")

        // TODO: Before enabling the My Location layer, you must request
        // location permission from the user. This sample does not include
        // a request for location permission.
        mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.setOnMyLocationButtonClickListener(this@LocationActivity)
        mGoogleMap.setOnMyLocationClickListener(this@LocationActivity)

    }

    private fun updateUI() {
        Timber.d("updateUI()")

    }

    private fun launchCoroutine() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                supervisorScope {
                    mViewModel.getItinerary(CHRONOPOST_LOCATION, BELTOISE_RACE_TRACK_LOCATION)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    override fun onMyLocationClick(location: Location) {
        Timber.e("onMyLocationClick() - $location")
        // Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()

        BottomSheetLocationFragment
            .newInstance(location)
            .show(
                supportFragmentManager,
                BottomSheetLocationFragment.TAG
            )
    }

    override fun onMyLocationButtonClick(): Boolean {
        Timber.d("onMyLocationButtonClick()")
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }
}