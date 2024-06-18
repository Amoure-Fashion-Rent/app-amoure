package com.amoure.amoure.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.CategoryItem
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.response.CategoryResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _categoryResults = MutableLiveData<List<CategoryItem?>>()
    val categoryResults: LiveData<List<CategoryItem?>> = _categoryResults

    private val _categoryClick = MutableLiveData<List<ProductItem?>>()
    val categoryClick: LiveData<List<ProductItem?>> = _categoryClick

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

    fun getCategory(name: String) {
        getCategory(accessToken, name)
    }

    fun getCategorybyId(id: String) {
        getCategory(accessToken, id)
    }

    private fun getCategory(token: String, name: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getAllCategory(name)
        client.enqueue(object : Callback<InitialResponse<CategoryResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<CategoryResponse>>,
                response: Response<InitialResponse<CategoryResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.product.let {
                        _categoryResults.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<CategoryResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }

    private fun getCategorybyId(token: String, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getAllCategorybyId(id)
        client.enqueue(object : Callback<InitialResponse<CategoryResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<CategoryResponse>>,
                response: Response<InitialResponse<CategoryResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.product.let {
                        _categoryResults.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<CategoryResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }


}