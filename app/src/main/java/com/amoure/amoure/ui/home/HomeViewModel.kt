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
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.retrofit.ApiService

class HomeViewModel(private val userRepository: UserRepository, private val productRepository: ProductRepository) : ViewModel() {
    var forYouProducts: LiveData<PagingData<ProductItem>> = MutableLiveData()

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private var apiService: ApiService? = null

    init {
        getForYouProducts()
    }

    private fun getForYouProducts() {
        forYouProducts = productRepository.getProducts(apiService, ProductPSParams()).cachedIn(viewModelScope)
    }
}