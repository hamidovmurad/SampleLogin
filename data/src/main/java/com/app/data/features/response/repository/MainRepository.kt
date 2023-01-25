package com.app.data.features.response.repository

import com.app.data.base.response.BaseResponse
import com.app.data.base.util.ResultWrapper
import com.app.data.features.request.AuthenticationBody
import com.app.data.features.request.PartnershipsBody
import com.app.data.features.response.AuthenticationResponse
import retrofit2.Response

interface MainRepository {

    suspend fun authentication(body: AuthenticationBody): ResultWrapper<Response<AuthenticationResponse>>

    suspend fun partnerships(body: PartnershipsBody): ResultWrapper<Response<BaseResponse>>

}

