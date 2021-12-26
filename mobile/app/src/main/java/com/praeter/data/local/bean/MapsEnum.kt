package com.praeter.data.local.bean

/**
 * Distance values using camera zoom for the map.
 * According to Android Maps SDK Website : https://developers.google.com/maps/documentation/android-sdk/views
 */
enum class MapsEnum(val distance: Float) {
    WORLD(1.0f),
    LANDMASS(5.0f),
    CITY(10.0f),
    STREETS(15.0f),
    BUILDINGS(20.0f),
    DEFAULT_MAX_ZOOM(19.0f);
}