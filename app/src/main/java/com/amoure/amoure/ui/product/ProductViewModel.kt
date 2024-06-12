package com.amoure.amoure.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductResponse
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _similarItems = MutableLiveData<List<ProductItem?>>()
    val similarItems: LiveData<List<ProductItem?>> = _similarItems

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem> = _product

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
                    getSimilarItems()
                }
            }
        }
    }

    fun getProduct(productId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getProductById(productId)
        client.enqueue(object : Callback<InitialResponse<ProductResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductResponse>>,
                response: Response<InitialResponse<ProductResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.product?.let {
                        _product.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<ProductResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }

    private fun getSimilarItems() {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getProducts()
        client.enqueue(object : Callback<InitialResponse<ProductsResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductsResponse>>,
                response: Response<InitialResponse<ProductsResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.products.let {
                        _similarItems.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<ProductsResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }
}