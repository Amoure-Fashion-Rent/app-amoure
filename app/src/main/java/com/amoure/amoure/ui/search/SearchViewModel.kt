package com.amoure.amoure.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.ProductRepository
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val userRepository: UserRepository, private val productRepository: ProductRepository) : ViewModel() {
    private val _searchResults = MutableLiveData<List<ProductItem?>>()
    val searchResults: LiveData<List<ProductItem?>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String

    init {
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                }
            }
        }
    }

    fun getSearch(query: String) {
//        searchResults =
//            productRepository.getProducts(ProductPSParams(search = query)).cachedIn(viewModelScope)
    }

    fun postVisSearch(multipartBody: MultipartBody.Part) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).postSearchByVisSearch(multipartBody)
        client.enqueue(object : Callback<InitialResponse<ProductsResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductsResponse>>,
                response: Response<InitialResponse<ProductsResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.products.let {
                        _searchResults.value = it
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<ProductsResponse>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}