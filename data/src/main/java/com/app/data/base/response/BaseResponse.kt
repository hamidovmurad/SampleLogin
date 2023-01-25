package com.app.data.base.response

import com.google.gson.annotations.SerializedName

open class BaseResponse {

    @SerializedName("message")
    val message: String? = null
    @SerializedName("code")
    val resultCode: String? = null

}

