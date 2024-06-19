package com.amoure.amoure.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.request.PostWishlistRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
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

    private val _wishlistResponse = MutableLiveData<InitialResponse<IdResponse>>()
    val wishlistResponse: LiveData<InitialResponse<IdResponse>> = _wishlistResponse

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var accessToken: String? = null

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

    fun getProduct(productId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    val client = ApiConfig.getApiService(it.accessToken).getProductById(productId)
                    client.enqueue(object : Callback<InitialResponse<ProductItem>> {
                        override fun onResponse(
                            call: Call<InitialResponse<ProductItem>>,
                            response: Response<InitialResponse<ProductItem>>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.data?.let { product ->
                                    _product.value = product
                                }
                                _isError.value = false
                            } else {
                                _isError.value = true
                            }
                            _isLoading.value = false
                        }

                        override fun onFailure(call: Call<InitialResponse<ProductItem>>, t: Throwable) {
                            _isError.value = true
                            _isLoading.value = false
                        }
                    })
                }
            }
        }
    }

    fun postWishlist(productId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    val client = ApiConfig.getApiService(it.accessToken).postWishlist(
                        PostWishlistRequest(productId)
                    )
                    client.enqueue(object : Callback<InitialResponse<IdResponse>> {
                        override fun onResponse(
                            call: Call<InitialResponse<IdResponse>>,
                            response: Response<InitialResponse<IdResponse>>
                        ) {
                            _isError.value = !response.isSuccessful
                            _isLoading.value = false
                            if (response.isSuccessful) {
                                _wishlistResponse.value = InitialResponse("OK")
                            } else {
                                _wishlistResponse.value = InitialResponse("ERROR")
                            }
                        }

                        override fun onFailure(
                            call: Call<InitialResponse<IdResponse>>,
                            t: Throwable
                        ) {
                            _isError.value = true
                            _isLoading.value = false
                            _wishlistResponse.value = InitialResponse("ERROR")
                        }
                    })
                }
            }
        }
    }

    private fun getSimilarItems() {
        _isLoading.value = true
//        val client = ApiConfig.getApiService(accessToken).getProducts()
        // TODO: API ML
//        client.enqueue(object : Callback<InitialResponse<ProductsResponse>> {
//            override fun onResponse(
//                call: Call<InitialResponse<ProductsResponse>>,
//                response: Response<InitialResponse<ProductsResponse>>
//            ) {
//                if (response.isSuccessful) {
//                    response.body()?.data?.products.let {
//                        _similarItems.value = it
//                    }
//                    _isError.value = false
//                } else {
//                    _isError.value = true
//                }
//                _isLoading.value = false
//            }
//
//            override fun onFailure(call: Call<InitialResponse<ProductsResponse>>, t: Throwable) {
//                _isError.value = true
//                _isLoading.value = false
//            }
//        })
    }
}