package com.example.gp.api

import com.example.gp.model.LoginRequest
import com.example.gp.model.LoginResponse
import com.example.gp.register.RegisterRequest
import com.example.gp.model.RegisterResponse
import com.example.gp.settings.UpdateProfile
import com.example.gp.settings.UpdateRequest
import com.example.gp.settings.profileResponse
import retrofit2.Response
import retrofit2.http.*

interface SimpleApi {
    @POST("api/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest) : Response<LoginResponse>

//
//    @POST("api/login")
//    suspend fun loginUser(
//        @Body body: Map<String, String>
//    ): Response<LoginResponse>


    @POST("api/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("api/logout")
    suspend fun logout(@Header("Authorization") auth: String)

    @POST("api/profile")
    suspend fun profile(@Header("Authorization") auth: String): Response<profileResponse>

    @POST("api/update_profile")
    suspend fun updateProfile(@Header("Authorization") auth: String, @Body updateRequest: UpdateRequest): Response<UpdateProfile>

}