package com.amoure.amoure.ui.search

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
import kotlinx.coroutines.launch

class SearchViewModel(private val userRepository: UserRepository, private val productRepository: ProductRepository) : ViewModel() {
    var searchResults: LiveData<PagingData<ProductItem>> = MutableLiveData()

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
        searchResults = productRepository.getProducts(ProductPSParams(search = query)).cachedIn(viewModelScope)
    }
}