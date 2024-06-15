package com.amoure.amoure.ui.designer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DesignerViewModel(private val repository: UserRepository) : ViewModel() {
    private val _products = MutableLiveData<List<ProductItem?>>()
    val products: LiveData<List<ProductItem?>> = _products

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

    private fun getProducts(ownerId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getProductsByOwner(ownerId)
        client.enqueue(object : Callback<InitialResponse<ProductsResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductsResponse>>,
                response: Response<InitialResponse<ProductsResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.products.let {
                        _products.value = it
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