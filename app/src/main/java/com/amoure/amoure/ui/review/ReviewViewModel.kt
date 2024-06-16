package com.amoure.amoure.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.request.PostReviewRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.data.response.ReviewResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewViewModel(private val repository: UserRepository) : ViewModel() {
    private val _reviews = MutableLiveData<List<ReviewItem?>>()
    val reviews: LiveData<List<ReviewItem?>> = _reviews

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String
    private lateinit var userId: String

    init {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    userId = it.userId
                }
            }
        }
    }

    fun getReviews(productId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getReviews(productId)
        client.enqueue(object : Callback<InitialResponse<ReviewResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ReviewResponse>>,
                response: Response<InitialResponse<ReviewResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.reviewResponse?.let {
                        _reviews.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<ReviewResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }

    fun postReview(productId: String, req: PostReviewRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).postReview(
            userId,
            productId,
            req.rating,
            req.comment,
            req.createdAt
            )
        client.enqueue(object : Callback<InitialResponse<IdResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<IdResponse>>,
                response: Response<InitialResponse<IdResponse>>
            ) {
                _isError.value = !response.isSuccessful
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<IdResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }

    fun deleteReview(productId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).deleteReview(
            userId,
            productId,
        )
        client.enqueue(object : Callback<InitialResponse<IdResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<IdResponse>>,
                response: Response<InitialResponse<IdResponse>>
            ) {
                _isError.value = !response.isSuccessful
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<IdResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }
}