package com.app.data.base.network.interceptors

import android.util.Log
import com.app.data.features.response.repository.UserDataRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenManagerImpl @Inject constructor(
    private val userDataRepository: UserDataRepository
) : TokenManager {

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val SESSION_ID = "Session-Id"
        const val BEARER = "Bearer"
        private const val ACCEPT_LANGUAGE = "Accept-Language"
    }

    override val accessToken: String? get() = userDataRepository.getToken()
    override val refreshToken: String? get() = userDataRepository.getRefreshToken()


    override fun setAccessToken(token: String?) {
        token?.let { userDataRepository.setToken(it) }
    }

    override fun setRefreshToken(token: String?) {
        token?.let { userDataRepository.setRefreshToken(it) }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.header(ACCEPT_LANGUAGE, userDataRepository.getLanguageApi())

        Log.i("TAG", "intercept $ACCEPT_LANGUAGE: ${userDataRepository.getLanguageApi()}")

        val token = accessToken
        Log.w("TAG", "intercept Token: $token")
        token?.let {
            request.header(AUTHORIZATION, "$BEARER $token")
        }
        return chain.proceed(request.build())
    }


}