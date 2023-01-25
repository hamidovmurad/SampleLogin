package com.app.data.features.service

import com.app.data.base.response.BaseResponse
import com.app.data.base.response.RefreshTokenResponse
import com.app.data.features.request.AuthenticationBody
import com.app.data.features.request.PartnershipsBody
import com.app.data.features.request.RefreshTokenRequest
import com.app.data.features.response.AuthenticationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainService {

    @POST("GetNewToken")
    suspend fun refreshToken(@Body body: RefreshTokenRequest): Response<RefreshTokenResponse>


    @POST("api/v1/authentication")
    suspend fun authentication(@Body body: AuthenticationBody): Response<AuthenticationResponse>

    @POST("api/v1/partnerships")
    suspend fun partnerships(@Body body: PartnershipsBody): Response<BaseResponse>



}