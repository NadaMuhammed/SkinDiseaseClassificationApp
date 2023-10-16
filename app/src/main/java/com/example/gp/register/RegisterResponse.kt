package com.example.gp.model

data class RegisterResponse(
    val message: String,
    val user: User
)

data class User(
    val created_at: String,
    val email: String,
    val fristname: String,
    val gender: String,
    val id: Int,
    val lastname: String,
    val updated_at: String,
    val username: String
)