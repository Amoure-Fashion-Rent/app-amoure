package com.amoure.amoure.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

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

    fun logout() {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    val client = ApiConfig.getApiService(it.accessToken).logout()
                    client.enqueue(object : Callback<InitialResponse<IdResponse>> {
                        override fun onResponse(
                            call: Call<InitialResponse<IdResponse>>,
                            response: Response<InitialResponse<IdResponse>>
                        ) {
                            _isError.value = !response.isSuccessful
                        }

                        override fun onFailure(
                            call: Call<InitialResponse<IdResponse>>,
                            t: Throwable
                        ) {
                            _isError.value = true
                        }
                    })
                    repository.logout()
                }
            }
        }
    }
}