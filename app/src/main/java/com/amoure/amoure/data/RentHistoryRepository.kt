package com.amoure.amoure.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.amoure.amoure.data.pagingsource.RentHistoryPSParams
import com.amoure.amoure.data.pagingsource.RentHistoryPagingSource
import com.amoure.amoure.data.pref.UserModel
import com.amoure.amoure.data.pref.UserPreference
import com.amoure.amoure.data.response.RentItem
import com.amoure.amoure.data.retrofit.ApiConfig
import com.amoure.amoure.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class RentHistoryRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun getSession(): UserModel {
        return userPreference.getSession().first()
    }

    suspend fun getApiService(): ApiService {
        val session = getSession()
        return ApiConfig.getApiService(session.accessToken)
    }

    fun getRents(params: RentHistoryPSParams): LiveData<PagingData<RentItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                RentHistoryPagingSource(runBlocking { getApiService() }, params)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: RentHistoryRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): RentHistoryRepository =
            instance ?: synchronized(this) {
                instance ?: RentHistoryRepository(userPreference)
            }.also { instance = it }
    }
}