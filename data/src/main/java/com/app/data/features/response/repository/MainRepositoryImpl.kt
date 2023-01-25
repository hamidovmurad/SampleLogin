package com.app.data.features.response.repository

import android.content.Context
import com.app.data.base.repository.BaseRepository
import com.app.data.base.response.BaseResponse
import com.app.data.base.util.ResultWrapper
import com.app.data.features.request.AuthenticationBody
import com.app.data.features.request.PartnershipsBody
import com.app.data.features.response.AuthenticationResponse
import com.app.data.features.service.MainService
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val service: MainService
) : BaseRepository(context), MainRepository {

    override suspend fun authentication(body: AuthenticationBody): ResultWrapper<Response<AuthenticationResponse>> {
        return handleNetwork { service.authentication(body) }
    }

    override suspend fun partnerships(body: PartnershipsBody): ResultWrapper<Response<BaseResponse>> {
        return handleNetwork { service.partnerships(body) }
    }
}
