package com.amoure.amoure.ui.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.Wishlist
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.response.WishlistResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishlistViewModel(private val repository: UserRepository) : ViewModel() {
    private val _wishListResults = MutableLiveData<Wishlist>()
    val wishlist: LiveData<Wishlist> = _wishListResults

    private val _products = MutableLiveData<List<ProductItem>>()
    val products: LiveData<List<ProductItem>> = _products
    private var productArray: MutableList<ProductItem> = arrayListOf()

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private lateinit var accessToken: String
    private lateinit var userId: String

    init {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    userId = it.userId
                    getWishlist(it.accessToken, it.userId)
                }
            }
        }
    }

    fun getWishlist(name: String) {
        getWishlist(accessToken, name)
    }

    private fun getWishlist(token: String, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getUserWishlist(id)
        client.enqueue(object : Callback<InitialResponse<WishlistResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<WishlistResponse>>,
                response: Response<InitialResponse<WishlistResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.wishlist?.let {
                        _wishListResults.value = it
                        it.productId?.let { ids ->
                            for (productId in ids) {
                                getWishlist(productId)
                            }
                        }
                        _products.value = productArray
                        _isLoading.value = false
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<InitialResponse<WishlistResponse>>, t: Throwable) {
                _isError.value = true
                _isLoading.value = false
            }
        })
    }

    fun deleteFromWishlist(productId: String) {
        deleteFromWishlist(accessToken, userId, productId)
    }

    private fun deleteFromWishlist(token: String, userId: String, productId: String) {
        val client = ApiConfig.getApiService(token).deleteFromWishlist(userId, productId)
        client.enqueue(object : Callback<InitialResponse<IdResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<IdResponse>>,
                response: Response<InitialResponse<IdResponse>>
            ) {
                _isError.value = !response.isSuccessful
            }

            override fun onFailure(call: Call<InitialResponse<IdResponse>>, t: Throwable) {
                _isError.value = true
            }
        })
    }
}