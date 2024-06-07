package com.amoure.amoure.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pref.UserModel
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: UserRepository) : ViewModel() {
    private val _trendingProducts = MutableLiveData<List<ProductItem?>>()
    val trendingProducts: LiveData<List<ProductItem?>> = _trendingProducts

    private val _forYouProducts = MutableLiveData<List<ProductItem?>>()
    val forYouProducts: LiveData<List<ProductItem?>> = _forYouProducts

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private lateinit var accessToken: String

    init {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    getTrending(it.accessToken)
                    getForYou(it.accessToken)
                }
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getTrending() {
        getTrending(accessToken)
    }

    private fun getTrending(token: String) {
        val client = ApiConfig.getApiService(token).getProducts()
        client.enqueue(object : Callback<InitialResponse<ProductsResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductsResponse>>,
                response: Response<InitialResponse<ProductsResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.products.let {
                        _trendingProducts.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<InitialResponse<ProductsResponse>>, t: Throwable) {
                _isError.value = true
            }
        })
    }

    private fun getForYou(token: String) {
        val client = ApiConfig.getApiService(token).getProducts()
        client.enqueue(object : Callback<InitialResponse<ProductsResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductsResponse>>,
                response: Response<InitialResponse<ProductsResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.products.let {
                        _forYouProducts.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<InitialResponse<ProductsResponse>>, t: Throwable) {
                _isError.value = true
            }
        })
    }
}