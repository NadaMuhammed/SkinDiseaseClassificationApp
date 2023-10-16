package com.example.gp.repository

import com.example.gp.api.RetrofitInstance
import com.example.gp.model.LoginRequest
import com.example.gp.model.LoginResponse
import com.example.gp.register.RegisterRequest
import com.example.gp.model.RegisterResponse
import com.example.gp.register.RegisterResponseUnAuthorized
import com.example.gp.settings.UpdateProfile
import com.example.gp.settings.UpdateRequest
import com.example.gp.settings.profileResponse
import retrofit2.Response

class Repository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>{
        return RetrofitInstance.api.loginUser(loginRequest)
    }

//    suspend fun loginUser(map: Map<String,String>): Response<LoginResponse>{
//        return RetrofitInstance.api.loginUser(map)
//    }

    suspend fun registerUser(registerRequest: RegisterRequest): Response<RegisterResponse>{
        return RetrofitInstance.api.registerUser(registerRequest)
    }

    suspend fun userProfile(auth: String): Response<profileResponse>{
        return RetrofitInstance.api.profile(auth)
    }

    suspend fun updateProfile(auth: String, updateRequest: UpdateRequest): Response<UpdateProfile>{
        return RetrofitInstance.api.updateProfile(auth,updateRequest)
    }
}