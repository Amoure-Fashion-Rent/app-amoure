package com.amoure.amoure.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amoure.amoure.data.ReviewRepository
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pagingsource.ReviewPSParams
import com.amoure.amoure.data.request.PostReviewRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class ReviewViewModel(private val userRepository: UserRepository, private val reviewRepository: ReviewRepository) : ViewModel() {
    var reviews: LiveData<PagingData<ReviewItem>> = MutableLiveData()

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String
    private var userId by Delegates.notNull<Int>()

    init {
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    userId = it.userId
                }
            }
        }
    }

    fun getReviews(productId: Int) {
        reviews = reviewRepository.getReviews(ReviewPSParams(productId = productId)).cachedIn(viewModelScope)
    }

    fun postReview(req: PostReviewRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    val client = ApiConfig.getApiService(it.accessToken).postReview(
                        req
                    )
                    client.enqueue(object : Callback<InitialResponse<ReviewItem>> {
                        override fun onResponse(
                            call: Call<InitialResponse<ReviewItem>>,
                            response: Response<InitialResponse<ReviewItem>>
                        ) {
                            _isError.value = !response.isSuccessful
                            _isLoading.value = false
                        }

                        override fun onFailure(
                            call: Call<InitialResponse<ReviewItem>>,
                            t: Throwable
                        ) {
                            _isError.value = true
                            _isLoading.value = false
                        }
                    })
                }
            }
        }
    }

    fun deleteReview(productId: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).deleteReview(
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