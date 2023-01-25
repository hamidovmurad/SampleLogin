package com.app.data.features.request

import com.google.gson.annotations.SerializedName

data class AuthenticationBody (
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)