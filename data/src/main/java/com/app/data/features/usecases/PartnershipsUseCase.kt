package com.app.data.features.usecases

import com.app.data.base.response.BaseResponse
import com.app.data.base.util.ResultWrapper
import com.app.data.features.response.repository.MainRepository
import com.app.data.features.request.PartnershipsBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class PartnershipsUseCase @Inject constructor(private val repository: MainRepository){

    operator fun invoke(body: PartnershipsBody): Flow<ResultWrapper<Response<BaseResponse>>> =
        flow {
            emit(ResultWrapper.Loading())
            emit(repository.partnerships(body))
        }
}