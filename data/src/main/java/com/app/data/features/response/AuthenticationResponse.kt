package com.app.data.features.response

data class AuthenticationResponse(
    val token: String,
    val user: User
)