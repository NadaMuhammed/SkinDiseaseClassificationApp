package com.example.gp.model

data class LoginResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val type: List<String>
)