package com.amoure.amoure.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.ProductRepository
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.CategoryItem
import com.amoure.amoure.data.response.ImageResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.SearchResponse
import com.amoure.amoure.data.response.VisSearchResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val userRepository: UserRepository, private val productRepository: ProductRepository) : ViewModel() {
    private val _searchResults = MutableLiveData<List<ProductItem?>>()
    val searchResults: LiveData<List<ProductItem?>> = _searchResults

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category

    private val _categoryResults = MutableLiveData<List<CategoryItem?>>()
    val categoryResults: LiveData<List<CategoryItem?>> = _categoryResults

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _combinedVariables = MediatorLiveData<Pair<List<CategoryItem?>, String>>()
    val combinedVariables: LiveData<Pair<List<CategoryItem?>, String>> = _combinedVariables

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
        _isLoading.value = true
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    val client = ApiConfig.getApiService(accessToken).searchByQuery(query)
                    client.enqueue(object : Callback<InitialResponse<SearchResponse>> {
                        override fun onResponse(
                            call: Call<InitialResponse<SearchResponse>>,
                            response: Response<InitialResponse<SearchResponse>>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.data?.products?.products?.let { products ->
                                    _searchResults.value = products
                                }
                                _isError.value = false
                            } else {
                                _isError.value = true
                            }
                            _isLoading.value = false
                        }

                        override fun onFailure(call: Call<InitialResponse<SearchResponse>>, t: Throwable) {
                            _isLoading.value = false
                            _isError.value = true
                        }
                    })
                }
            }
        }
    }

    fun postVisSearch(multipartBody: MultipartBody.Part) {
        _isLoading.value = true
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    getCategory()
                    val client = ApiConfig.getApiService(it.accessToken).postMlImage(multipartBody)
                    client.enqueue(object : Callback<InitialResponse<ImageResponse>> {
                        override fun onResponse(
                            call: Call<InitialResponse<ImageResponse>>,
                            response: Response<InitialResponse<ImageResponse>>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.data?.imageUrl?.let { url ->
                                    visSearch(url)
                                }
                                _isError.value = false
                            } else {
                                _isError.value = true
                                _isLoading.value = false
                            }
                        }

                        override fun onFailure(call: Call<InitialResponse<ImageResponse>>, t: Throwable) {
                            _isLoading.value = false
                            _isError.value = true
                        }
                    })
                }
            }
        }
    }

    private fun visSearch(imageUrl: String) {
        val client = ApiConfig.getApiService(accessToken).searchByImage(imageUrl)
        client.enqueue(object : Callback<InitialResponse<VisSearchResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<VisSearchResponse>>,
                response: Response<InitialResponse<VisSearchResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.similarProducts?.products?.let {
                        _searchResults.value = it
                    }
                    response.body()?.data?.category?.let {
                        _category.value = it
                    }
                    _combinedVariables.addSource(_category) { value1 ->
                        val value2 = _categoryResults.value
                        if (value1 != null && value2 != null) {
                            _combinedVariables.value = Pair(value2, value1)
                        }
                    }
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<VisSearchResponse>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    private fun getCategory() {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getAllCategory()
        client.enqueue(object : Callback<InitialResponse<List<CategoryItem>>> {
            override fun onResponse(
                call: Call<InitialResponse<List<CategoryItem>>>,
                response: Response<InitialResponse<List<CategoryItem>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _categoryResults.value = it
                    }
                    _combinedVariables.addSource(_categoryResults) { value1 ->
                        val value2 = _category.value
                        if (value1 != null && value2 != null) {
                            _combinedVariables.value = Pair(value1, value2)
                        }
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<InitialResponse<List<CategoryItem>>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }
}