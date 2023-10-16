package com.example.gp.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gp.model.LoginRequest
import com.example.gp.model.LoginResponse
import com.example.gp.register.RegisterRequest
import com.example.gp.model.RegisterResponse
import com.example.gp.register.RegisterResponseUnAuthorized
import com.example.gp.repository.Repository
import com.example.gp.settings.UpdateProfile
import com.example.gp.settings.UpdateRequest
import com.example.gp.settings.profileResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var loginResponse : MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    var registerResponse : MutableLiveData<Response<RegisterResponse>> = MutableLiveData()
    var profileResponse : MutableLiveData<Response<profileResponse>> = MutableLiveData()
    var updateProfile: MutableLiveData<Response<UpdateProfile>> = MutableLiveData()
    var repository = Repository()

    fun loginRequest(loginRequest: LoginRequest){
        viewModelScope.launch{
            loginResponse.value = repository.loginUser(loginRequest)
        }
    }

    fun registerUser(registerRequest: RegisterRequest){
        viewModelScope.launch {
            registerResponse.value = repository.registerUser(registerRequest)
        }
    }

    fun userProfile(auth: String){
        viewModelScope.launch {
            profileResponse.value = repository.userProfile(auth)
        }
    }

    fun updateProfile(auth: String, updateRequest: UpdateRequest){
        viewModelScope.launch {
            updateProfile.value = repository.updateProfile(auth, updateRequest)
        }
    }
}