package com.amoure.amoure.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.request.PutCartRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductsResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(private val repository: UserRepository) : ViewModel() {
    private val _carts = MutableLiveData<ProductsResponse>()
    val carts: LiveData<ProductsResponse> = _carts

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private lateinit var accessToken: String
    private lateinit var userId: String

    init {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    userId = it.userId
                    getCart(it.accessToken, it.userId)
                }
            }
        }
    }
    fun getCart() {
        getCart(accessToken, userId)
    }

    private fun getCart(token: String, id: String) {
        val client = ApiConfig.getApiService(token).getUserCart(id)
        client.enqueue(object : Callback<InitialResponse<ProductsResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductsResponse>>,
                response: Response<InitialResponse<ProductsResponse>>
            ) {
                if (response.isSuccessful) {
                    // TODO: Correct?, change ProductsResponse to CartResponse
                    response.body()?.data?.let {
                        _carts.value = it
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<InitialResponse<ProductsResponse>>, t: Throwable) {
                _isError.value = true
            }
        })
    }

    fun putFromCart(req: PutCartRequest) {
        putFromCart(accessToken, userId, req)
    }

    private fun putFromCart(token: String, id: String, req: PutCartRequest) {
        val client = ApiConfig.getApiService(token).putFromCart(
            id,
            req.delivery,
            req.deliveryPrice,
            req.cardNumber,
            req.cardCVV,
            req.cardExpiry
        )
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

    fun deleteFromCart(productId: String) {
        deleteFromCart(accessToken, userId, productId)
    }

    private fun deleteFromCart(token: String, userId: String, productId: String) {
        val client = ApiConfig.getApiService(token).deleteFromCart(userId, productId)
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