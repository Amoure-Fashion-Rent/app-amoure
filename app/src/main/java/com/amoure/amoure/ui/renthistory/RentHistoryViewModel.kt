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

class RentHistoryViewModel(private val userRepository: UserRepository, private val rentHistoryRepository: RentHistoryRepository) : ViewModel() {
    var rents: LiveData<PagingData<RentItem>> = MutableLiveData()

    init {
        getRents()
    }

    private fun getRents() {
        rents = rentHistoryRepository.getRents(RentHistoryPSParams()).cachedIn(viewModelScope)
    }
}