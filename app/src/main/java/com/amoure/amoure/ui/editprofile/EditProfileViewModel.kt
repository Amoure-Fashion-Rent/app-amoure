package com.amoure.amoure.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.request.PutProfileRequest
import com.amoure.amoure.data.response.IdResponse
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.Profile
import com.amoure.amoure.data.response.ProfileResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

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
                    getProfile()
                }
            }
        }
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

    fun putProfile(profile: PutProfileRequest) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).putProfile(
            userId,
            profile.fullName,
            profile.email,
            profile.addressDetail,
            profile.province,
            profile.city,
            profile.district,
            profile.postalCode,
            profile.phoneNumber,
            profile.birthDate
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