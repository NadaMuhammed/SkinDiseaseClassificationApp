package com.example.gp.register

data class RegisterRequest(
    var fristname: String,
    var lastname: String,
    var gender: String,
    var type: String,
    var username: String,
    var email: String,
    var password: String,
    var password_confirmation: String
)