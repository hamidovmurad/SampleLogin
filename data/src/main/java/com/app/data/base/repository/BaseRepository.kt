package com.app.data.base.repository

import android.content.Context
import com.app.data.base.network.interceptors.TokenManager
import com.app.data.base.response.RefreshTokenResponse
import com.app.data.base.util.ErrorResponse
import com.app.data.base.util.ResultWrapper
import com.app.data.features.response.repository.MainRepository
import com.app.data.features.response.repository.UserDataRepository
import com.google.gson.Gson
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRepository(
    private val context: Context
) {

    @Inject
    lateinit var refreshTokenManager: RefreshTokenManager

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Inject
    lateinit var tokenManager: TokenManager

    companion object {
        const val MESSAGE_SUCCESS: String = "success"
        const val GENERAL_ERROR_MESSAGE = "Texniki xəta baş verdi. Xahiş edirik bir daha cəhd edin."
        const val NETWORK_ERROR_MESSAGE = "Şəbəkə bağlantısını yoxlayın və yenidən cəhd edin"
        const val SESSION_ERROR_MESSAGE = "Sessiya bitmişdir"
    }


    suspend fun <T : Any> apiCallInternal(
        apiCall: suspend () -> Response<T>?
    ): ResultWrapper<Response<T>> {
        return try {
            val result = apiCall()
            if (result?.code() == 204 || result?.code() == 200){
                return ResultWrapper.Success(result)
            }
            if (result?.isSuccessful == true) {
                ResultWrapper.Success(result)
            } else {
                Gson().fromJson(
                    result?.errorBody()?.string(), ErrorResponse::class.java
                )?.let {
                  val errorBody =  if (it.code == null && result?.code() == 401) {
                        it.copy(code = 401)
                    } else it

                    ResultWrapper.Error(errorBody)

                }?: run{

                    val errorBody =  ErrorResponse()
                    errorBody.let {
                        if (result?.code() == 401) it.code = 401
                    }

                    ResultWrapper.Error(errorBody)
                }

            }
        } catch (throwable: Throwable) {
            val networkError = ErrorResponse(message = NETWORK_ERROR_MESSAGE)
            throwable.printStackTrace()
            when (throwable) {
                is SocketTimeoutException -> ResultWrapper.Error(
                    ErrorResponse(
                        message = GENERAL_ERROR_MESSAGE,
                        code = 522
                    )
                )
                is IOException -> ResultWrapper.Error(networkError)
                is ConnectException -> ResultWrapper.Error(networkError)
                is UnknownHostException -> ResultWrapper.Error(networkError)
                is HttpException -> {
                    val errorResponse = ErrorResponse(message = GENERAL_ERROR_MESSAGE)
                    ResultWrapper.Error(errorResponse)
                }
                else -> {
                    ResultWrapper.Error(
                        ErrorResponse(
                            message = GENERAL_ERROR_MESSAGE
                        )
                    )
                }
            }
        }
    }


    suspend inline fun <reified T : Any> handleNetwork(
        crossinline apiCall: suspend () -> Response<T>
    ): ResultWrapper<Response<T>> {
        val wrapper = apiCallInternal {
            apiCall()
        }
        return if (this !is MainRepository
            && wrapper is ResultWrapper.Error
            && wrapper.error.code == 401
        ) {

            handleRefreshTokenResult {
                apiCall()
            }
        } else {
            wrapper
        }
    }

    suspend fun <T : Any> handleRefreshTokenResult(apiCall: suspend () -> Response<T>?): ResultWrapper<Response<T>> {


       return when (val tokenInfo = getToken()) {
            is ResultWrapper.Success -> {
                val body = tokenInfo.data?.body()
                body?.jwt?.let { token ->
                    tokenManager.setAccessToken(token)
                }
                body?.refreshToken?.let { token ->
                    tokenManager.setRefreshToken(token)
                }
                apiCallInternal {
                    apiCall()
                }
            }
            is ResultWrapper.Error -> {
                ResultWrapper.Error(tokenInfo.error)
            }
            else -> ResultWrapper.Error(ErrorResponse(message = NETWORK_ERROR_MESSAGE))
        }
    }

    private suspend fun getToken(): ResultWrapper<Response<RefreshTokenResponse>?> {
        return suspendCancellableCoroutine { continuation ->
            refreshTokenManager.refreshToken(object : RefreshTokenManager.WaitingCallBack {
                override suspend fun onResponse(result: ResultWrapper<Response<RefreshTokenResponse>?>) {
                    if (continuation.isActive) {
                        try {
                            continuation.resume(result) {
                                Timber.e(it)
                            }

                        } catch (ex: Exception) {
                            Timber.e(ex)
                        }
                    }
                }
            })
        }
    }
}