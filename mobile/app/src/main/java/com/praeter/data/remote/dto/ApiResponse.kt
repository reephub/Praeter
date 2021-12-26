package com.praeter.data.remote.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
class ApiResponse(
    @Json(name = "code")
    var code: Int = 0,

    @Json(name = "message")
    var message: String? = null
) : Parcelable {
    override fun toString(): String {
        return "ApiResponse(code=$code, message=$message)"
    }
}