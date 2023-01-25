package com.app.data.base.repository

import com.app.data.base.network.interceptors.TokenManager
import com.app.data.base.response.RefreshTokenResponse
import com.app.data.base.util.ErrorResponse
import com.app.data.base.util.ResultWrapper
import com.app.data.features.request.RefreshTokenRequest
import com.app.data.features.response.repository.MainRepository
import com.app.data.features.service.MainService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefreshTokenManager @Inject constructor(
    private val tokenManager: TokenManager,
    private val service: MainService
) {

    private var isRefreshActive = false

    private val waitingResponseStack =
        Collections.synchronizedList(mutableListOf<WaitingCallBack>())

    fun refreshToken(waitingCallBack: WaitingCallBack) {

        if (!waitingResponseStack.contains(waitingCallBack)) {
            waitingResponseStack.add(waitingCallBack)
        }

        synchronized(this) {
            if (!isRefreshActive) {
                isRefreshActive = true
                refreshTokenOperation()
            }
        }
    }

    private fun refreshTokenOperation() {
        GlobalScope.launch {
            val response = service.refreshToken(RefreshTokenRequest(tokenManager.refreshToken))
            if (response.isSuccessful) {
                isRefreshActive = false
                val data = response.body()
                try {
                    data?.jwt?.let { token ->
                        tokenManager.setAccessToken(token)
                    }
                    data?.refreshToken?.let { token ->
                        tokenManager.setRefreshToken(token)
                    }
                    sendApiResult(ResultWrapper.Success(response))
                } catch (ignored: Exception) {
                    Timber.e(ignored, "refresh token ignored ")
                }

            } else {
                try {
                    if (waitingResponseStack.size > 0)
                        waitingResponseStack.last().onResponse(
                            ResultWrapper.Error(
                                ErrorResponse(
                                    code = response.code(),
                                    message = BaseRepository.GENERAL_ERROR_MESSAGE
                                )
                            )
                        )
                } catch (e: Exception) {
                    Timber.e(e)
                }
                isRefreshActive = false
            }
        }
    }

    private suspend fun sendApiResult(result: ResultWrapper<Response<RefreshTokenResponse>?>) {
        waitingResponseStack.forEach {
            it.onResponse(result)
        }
        waitingResponseStack.clear()
    }

    interface WaitingCallBack {
        suspend fun onResponse(result: ResultWrapper<Response<RefreshTokenResponse>?>)
    }
}