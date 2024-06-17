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
import com.amoure.amoure.data.response.Profile
import com.amoure.amoure.data.response.ProfileResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class CartViewModel(private val repository: UserRepository) : ViewModel() {
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _carts = MutableLiveData<Cart>()
    val carts: LiveData<Cart> = _carts

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String
    private var userId by Delegates.notNull<Int>()

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

    private fun getCart(token: String, id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getUserCart(id)
        client.enqueue(object : Callback<InitialResponse<CartResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<CartResponse>>,
                response: Response<InitialResponse<CartResponse>>
            ) {
                if (response.isSuccessful) {

                    response.body()?.data?.carts?.let {
                        _carts.value = it
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

    fun putFromCart(req: PutCartRequest) {
        putFromCart(accessToken, userId, req)
    }

    private fun putFromCart(token: String, id: Int, req: PutCartRequest) {
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

    private fun deleteFromCart(token: String, userId: Int, productId: String) {
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

    private fun getProfile() {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getProfile(userId)
        client.enqueue(object : Callback<InitialResponse<ProfileResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<ProfileResponse>>,
                response: Response<InitialResponse<ProfileResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.user?.let {
                        _profile.value = it
                    }
                    _isLoading.value = false
                    _isError.value = false
                } else {
                    _isLoading.value = false
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<InitialResponse<ProfileResponse>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}