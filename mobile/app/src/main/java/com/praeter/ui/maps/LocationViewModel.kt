package com.praeter.ui.maps

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.praeter.data.IRepository
import com.praeter.data.remote.dto.directions.GoogleDirectionsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val googleDirection: MutableLiveData<GoogleDirectionsResponse> = MutableLiveData()
    private val address: MutableLiveData<Address> = MutableLiveData()

    fun getGoogleDirection(): LiveData<GoogleDirectionsResponse> {
        return googleDirection
    }

    fun getAddress(): LiveData<Address> {
        return address
    }

    suspend fun getItinerary(origin: String, destination: String) {
        val response = withContext(Dispatchers.IO) {
            val route = repository.getDirections(origin, destination)

            Timber.d("$route")

            route
        }

        Timber.e(response.toString())
        googleDirection.value = response
    }

    fun getAddressFromLocation(context: Context, location: Location) {
        val addresses: List<Address>
        val geocoder = Geocoder(context, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        address.value = addresses[0]
    }
}