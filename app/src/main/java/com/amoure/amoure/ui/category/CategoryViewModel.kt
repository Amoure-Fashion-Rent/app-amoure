package com.amoure.amoure.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amoure.amoure.data.ProductRepository
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pagingsource.ProductPSParams
import com.amoure.amoure.data.response.CategoryItem
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.retrofit.ApiConfig
import com.amoure.amoure.data.retrofit.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel(private val userRepository: UserRepository, private val productRepository: ProductRepository) : ViewModel() {
    private val _categoryResults = MutableLiveData<List<CategoryItem?>>()
    val categoryResults: LiveData<List<CategoryItem?>> = _categoryResults

    var productsCategory: LiveData<PagingData<ProductItem>> = MutableLiveData()

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String
    private var apiService: ApiService? = null

    init {
        getCategory()
    }

    private fun getCategory() {
        _isLoading.value = true
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    val client = ApiConfig.getApiService(it.accessToken).getAllCategory()
                    client.enqueue(object : Callback<InitialResponse<List<CategoryItem>>> {
                        override fun onResponse(
                            call: Call<InitialResponse<List<CategoryItem>>>,
                            response: Response<InitialResponse<List<CategoryItem>>>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.data?.let { categories ->
                                    _categoryResults.value = categories
                                }
                                _isError.value = false
                            } else {
                                _isError.value = true
                            }
                            _isLoading.value = false
                        }

                        override fun onFailure(call: Call<InitialResponse<List<CategoryItem>>>, t: Throwable) {
                            _isError.value = true
                            _isLoading.value = false
                        }
                    })
                }
            }
        }
    }

    fun getProductByCategory(id: Int) {
        productsCategory = productRepository.getProducts(apiService, ProductPSParams(categoryId = id)).cachedIn(viewModelScope)
    }


}