package com.amoure.amoure.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amoure.amoure.data.ProductRepository
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pagingsource.ProductPSParams
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userRepository: UserRepository, productRepository: ProductRepository) : ViewModel() {
    private val _trendingProducts = MutableLiveData<List<ProductItem?>>()
    val trendingProducts: LiveData<List<ProductItem?>> = _trendingProducts

    var forYouProducts: LiveData<PagingData<ProductItem>> = MutableLiveData()

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private lateinit var accessToken: String

    init {
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    getTrending()
                    forYouProducts = productRepository.getProducts(ProductPSParams()).cachedIn(viewModelScope)
                }
            }
        }
    }

    private fun getTrending() {
        val client = ApiConfig.getApiService(accessToken).getProductsCall()
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
}