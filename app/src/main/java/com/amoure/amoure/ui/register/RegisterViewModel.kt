package com.amoure.amoure.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amoure.amoure.data.request.RegisterRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val _response = MutableLiveData<InitialResponse<IdResponse>>()
    val response: LiveData<InitialResponse<IdResponse>> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(request: RegisterRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService("").register(request.fullName, request.email, request.password, request.role)
        client.enqueue(object : Callback<InitialResponse<IdResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<IdResponse>>,
                response: Response<InitialResponse<IdResponse>>
            ) {
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<IdResponse>>, t: Throwable) {
                _isLoading.value = false
                _response.value = InitialResponse("Please try again later\\! Server isn\\'t responding")
            }
        })
    }
}