package com.app.data.base.network.interceptors

import okhttp3.Interceptor

interface TokenManager : Interceptor {

    val accessToken: String?
    val refreshToken: String?

    fun setAccessToken(token: String?)
    fun setRefreshToken(token: String?)
}