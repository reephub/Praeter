package com.praeter.data.remote.dto.user

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
class User constructor(
    @Json(name = "gender")
    var gender: String? = null,

    @Json(name = "firstName")
    var firstName: String? = null,

    @Json(name = "lastName")
    var lastName: String? = null,

    @Json(name = "email")
    var email: String? = null,

    @Json(name = "password")
    var password: String? = null,

    @Json(name = "phoneNumber")
    var phoneNumber: String? = null,

    @Json(name = "dateOfBirth")
    var dateOfBirth: String? = null,

    @Json(name = "isPremium")
    var isPremium: Boolean = false,

    @Json(name = "isCustomer")
    var isCustomer: Boolean = false,

    @Json(name = "isProvider")
    var isProvider: Boolean = false
) : Parcelable {

    constructor(email: String, password: String) : this() {
        this.email = email
        this.password = password
    }

    override fun toString(): String {
        return "User(gender=$gender, firstName=$firstName, lastName=$lastName, email=$email, password=$password, phoneNumber=$phoneNumber, dateOfBirth=$dateOfBirth, isPremium=$isPremium, isCustomer=$isCustomer, isProvider=$isProvider)"
    }
}