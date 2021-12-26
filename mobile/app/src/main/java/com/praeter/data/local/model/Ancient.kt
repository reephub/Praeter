package com.praeter.data.local.model

import android.location.Location
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
class Ancient(
    var name: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var location: Location? = null
) : Parcelable {


    constructor(name: String) : this() {
        this.name = name
    }

    constructor(name: String, latitude: Double, longitude: Double) : this() {
        this.name = name
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor(name: String, location: Location) : this() {
        this.name = name
        this.location = location
        latitude = location.latitude
        longitude = location.longitude
    }
}