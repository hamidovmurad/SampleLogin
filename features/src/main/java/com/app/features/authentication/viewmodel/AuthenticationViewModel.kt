package com.app.features.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.base.response.BaseResponse
import com.app.data.base.util.ResultWrapper
import com.app.data.features.response.AuthenticationResponse
import com.app.data.features.usecases.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel(){


    private val _data = MutableStateFlow<ResultWrapper<Response<AuthenticationResponse>>?>(null)
    val data = _data.asStateFlow()

    fun authentication(
        username: String,
        password: String
    ) {
        authenticationUseCase.invoke(username,password).onEach {
            _data.emit(it)
        }.launchIn(viewModelScope)
    }


}