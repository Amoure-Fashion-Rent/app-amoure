package com.amoure.amoure.ui.renthistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.response.InitialResponse
import com.amoure.amoure.data.response.RentItem
import com.amoure.amoure.data.response.RentResponse
import com.amoure.amoure.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class RentHistoryViewModel(private val repository: UserRepository) : ViewModel() {
    private val _rents = MutableLiveData<List<RentItem?>>()
    val rents: LiveData<List<RentItem?>> = _rents

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
                    getRents()
                }
            }
        }
    }

    private fun getRents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService(accessToken).getRents(userId)
        // TODO: synchronize with backend
    }
}