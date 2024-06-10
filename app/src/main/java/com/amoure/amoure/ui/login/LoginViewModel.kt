package com.amoure.amoure.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pref.UserModel
import com.amoure.amoure.data.request.LoginRequest
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.LoginResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _response = MutableLiveData<InitialResponse<LoginResponse>>()
    val response: LiveData<InitialResponse<LoginResponse>> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(request: LoginRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService("").login(request.email, request.password)
        client.enqueue(object : Callback<InitialResponse<LoginResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<LoginResponse>>,
                response: Response<InitialResponse<LoginResponse>>
            ) {
                _response.value = response.body()
                if (response.isSuccessful) {
                    _response.value?.data?.let {
                        saveSession(UserModel(it.accessToken, it.userId, true, it.userType))
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<LoginResponse>>, t: Throwable) {
                _isLoading.value = false
                _response.value = InitialResponse("error", "Please try again later\\! Server isn\\'t responding")
            }
        })
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}