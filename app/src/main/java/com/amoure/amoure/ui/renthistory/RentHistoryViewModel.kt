package com.amoure.amoure.ui.renthistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amoure.amoure.data.RentHistoryRepository
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pagingsource.RentHistoryPSParams
import com.amoure.amoure.data.response.RentItem
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class RentHistoryViewModel(private val userRepository: UserRepository, private val rentHistoryRepository: RentHistoryRepository) : ViewModel() {
    var rents: LiveData<PagingData<RentItem>> = MutableLiveData()

    private lateinit var accessToken: String
    private var userId by Delegates.notNull<Int>()

    init {
        viewModelScope.launch {
            userRepository.getSession().collect {
                if (it.isLogin) {
                    accessToken = it.accessToken
                    userId = it.userId
                    getRents()
                }
            }
        }
    }

    fun getRents() {
        rents = rentHistoryRepository.getRents(RentHistoryPSParams()).cachedIn(viewModelScope)
    }
}