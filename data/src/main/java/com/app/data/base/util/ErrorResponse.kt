package com.app.data.base.util

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("message")
    val message: String? = null,
)
