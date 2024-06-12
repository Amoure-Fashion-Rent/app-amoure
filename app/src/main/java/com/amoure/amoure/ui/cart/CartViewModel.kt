package com.amoure.amoure.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.request.PutCartRequest
import com.amoure.amoure.data.response.Cart
import com.amoure.amoure.data.response.CartResponse
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.response.ProductResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(private val repository: UserRepository) : ViewModel() {
    private val _carts = MutableLiveData<Cart>()
    val carts: LiveData<Cart> = _carts

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
                    getCart(it.accessToken, it.userId)
                }
            }
        }
    }
    fun getCart() {
        getCart(accessToken, userId)
    }

    private fun getCart(token: String, id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getUserCart(id)
        client.enqueue(object : Callback<InitialResponse<CartResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<CartResponse>>,
                response: Response<InitialResponse<CartResponse>>
            ) {
                if (response.isSuccessful) {

                    response.body()?.data?.cart?.let {
                        _carts.value = it
                        it.productId?.let { ids ->
                            for (productId in ids) {
                                getProduct(productId)
                            }
                        }
                        _products.value = productArray
                        _isLoading.value = false
                    }
                    _isError.value = false
                } else {
                    _isLoading.value = false
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<InitialResponse<CartResponse>>, t: Throwable) {

                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    private fun getProduct(id:String) {
        val client = ApiConfig.getApiService(accessToken).getProductById(id)
        client.enqueue(object : Callback<InitialResponse<ProductResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProductResponse>>,
                response: Response<InitialResponse<ProductResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.product?.let {
                        productArray.add(it)
                    }
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<InitialResponse<ProductResponse>>, t: Throwable) {
                _isError.value = true
            }
        })
        return
    }

    fun putFromCart(req: PutCartRequest) {
        putFromCart(accessToken, userId, req)
    }

    private fun putFromCart(token: String, id: String, req: PutCartRequest) {
        val client = ApiConfig.getApiService(token).putFromCart(
            id,
            req.rentalStartDate,
            req.rentalEndDate,
            req.rentalDuration,
            req.delivery
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