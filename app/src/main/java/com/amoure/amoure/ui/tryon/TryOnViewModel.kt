package com.amoure.amoure.ui.review

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.ImageResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TryOnViewModel(private val repository: UserRepository) : ViewModel() {
    private val _tryOn = MutableLiveData<Bitmap>()
    val tryOn: LiveData<Bitmap> = _tryOn

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

    fun tryOn(multipartBody: MultipartBody.Part, productId: Int, category: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    val client = ApiConfig.getApiService(accessToken).postMlImage(multipartBody)
                    client.enqueue(object : Callback<InitialResponse<ImageResponse>> {
                        override fun onResponse(
                            call: Call<InitialResponse<ImageResponse>>,
                            response: Response<InitialResponse<ImageResponse>>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.data?.imageUrl?.let { url ->
                                    postTryOn(url, productId, category)
                                }
                                _isError.value = false
                            } else {
                                _isError.value = true
                                _isLoading.value = false
                            }
                        }

                        override fun onFailure(
                            call: Call<InitialResponse<ImageResponse>>,
                            t: Throwable
                        ) {
                            _isLoading.value = false
                            _isError.value = true
                        }
                    })
                }
            }
        }
    }


    fun postTryOn(vton: String, productId: Int, category: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).tryOn(vton, productId, category)
        client.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    _tryOn.value = BitmapFactory.decodeStream(
                        response.body()!!.byteStream()
                    )
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}