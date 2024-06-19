package com.amoure.amoure.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TryOnViewModel(private val repository: UserRepository) : ViewModel() {
    private val _tryOn = MutableLiveData<String>()
    val tryOn: LiveData<String> = _tryOn

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String

    init {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                }
            }
        }
    }

    fun postTryOn(vton: MultipartBody.Part, garm: MultipartBody.Part, category: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).postTryOn(vton, garm, category)
        client.enqueue(object : Callback<InitialResponse<String>> {
            override fun onResponse(
                call: Call<InitialResponse<String>>,
                response: Response<InitialResponse<String>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _tryOn.value = it
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<String>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}