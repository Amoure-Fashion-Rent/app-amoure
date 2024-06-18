package com.amoure.amoure.ui.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.request.PutProductRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.Profile
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductViewModel(private val repository: UserRepository) : ViewModel() {
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private lateinit var accessToken: String
    private lateinit var ownerId: String

    init {
        viewModelScope.launch {
            repository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    ownerId = it.userId

                }
            }
        }
    }


    fun putProduct(profile: PutProductRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).postProduct(
            ownerId,
            profile.name,
            profile.product,
            profile.details,
            profile.notes,
            profile.retail,
            profile.rent,
            profile.category,
            profile.sizes,
            profile.images,
        )
        client.enqueue(object : Callback<InitialResponse<IdResponse>> {
            override fun onResponse(
                call: Call<InitialResponse<IdResponse>>,
                response: Response<InitialResponse<IdResponse>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _isError.value = false
                } else {
                    _isLoading.value = false
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<InitialResponse<IdResponse>>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}