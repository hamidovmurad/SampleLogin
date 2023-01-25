package com.app.data.base.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("jwt")
    val jwt: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null
): BaseResponse()
