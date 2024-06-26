package com.amoure.amoure.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.WishlistItem
import com.amoure.amoure.data.response.WishlistResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishlistViewModel(private val repository: UserRepository) : ViewModel() {
    private val _products = MutableLiveData<List<WishlistItem?>>()
    val products: LiveData<List<WishlistItem?>> = _products

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
                    getWishlist()
                }
            }
        }
    }

    private fun getWishlist() {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getUserWishlist()
        client.enqueue(object : Callback<InitialResponse<WishlistResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<WishlistResponse>>,
                response: Response<InitialResponse<WishlistResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.wishlist?.let {
                        _products.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<WishlistResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }
}