package com.amoure.amoure.ui.designer

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

class DesignerViewModel(private val userRepository: UserRepository, private val productRepository: ProductRepository) : ViewModel() {
    var products: LiveData<PagingData<ProductItem>> = MutableLiveData()
    private var apiService: ApiService? = null

    fun getProducts(ownerId: Int) {
        products = productRepository.getProducts(apiService, ProductPSParams(ownerId = ownerId)).cachedIn(viewModelScope)
    }
}