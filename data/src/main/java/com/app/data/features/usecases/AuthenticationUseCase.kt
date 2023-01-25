package com.app.data.features.usecases

import com.app.data.base.util.ResultWrapper
import com.app.data.features.response.repository.MainRepository
import com.app.data.features.request.AuthenticationBody
import com.app.data.features.response.AuthenticationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(private val repository: MainRepository) {

    operator fun invoke(
        username: String,
        password: String
    ): Flow<ResultWrapper<Response<AuthenticationResponse>>> =
        flow {
            emit(ResultWrapper.Loading())
            emit(repository.authentication(AuthenticationBody(username,password)))
        }
}