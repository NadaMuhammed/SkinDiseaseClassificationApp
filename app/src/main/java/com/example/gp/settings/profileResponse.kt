package com.example.gp.settings

data class profileResponse(
    val created_at: String,
    val email: String,
    val email_verified_at: Any,
    val fristname: String,
    val gender: String,
    val id: Int,
    val lastname: String,
    val type: String,
    val updated_at: String,
    val username: String
)