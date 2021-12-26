package com.praeter.data.local.model

import android.location.Location
import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
class ClassModel(
    var name: String,
    var type: String,
    var duration: String,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var location: Location? = null,
) : Parcelable {

    constructor(name: String) : this(name, "", "", 0.0, 0.0, null) {
        this.name = name
    }

    constructor(name: String, type: String) : this(name, type, "", 0.0, 0.0, null) {
        this.name = name
        this.type = type
    }

    constructor(name: String, type: String, duration: String) : this(
        name,
        type,
        duration,
        0.0,
        0.0,
        null
    ) {
        this.name = name
        this.type = type
        this.duration = duration
    }

    constructor(
        name: String,
        type: String,
        duration: String,
        latitude: Double,
        longitude: Double
    ) : this(name, type, duration, latitude, longitude, null) {
        this.name = name
        this.type = type
        this.duration = duration
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor(name: String, type: String, duration: String, location: Location) : this(
        name,
        type,
        duration,
        location.latitude,
        location.longitude,
        location
    ) {
        this.name = name
        this.type = type
        this.duration = duration
        this.location = location
        latitude = location.latitude
        longitude = location.longitude
    }

    override fun toString(): String {
        return "ClassModel{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", duration='" + duration + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location=" + location +
                '}'
    }
}