package com.app.data.features.request

import com.google.gson.annotations.SerializedName

data class PartnershipsBody(
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("restaurant")
    val restaurant: String,
    @SerializedName("note")
    val note: String? = null
)
