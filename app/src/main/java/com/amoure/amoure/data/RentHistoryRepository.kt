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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class RentHistoryRepository private constructor(
    private val userPreference: UserPreference
) {
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getApiService(): ApiService {
        return runBlocking(Dispatchers.IO) {
            getSession().first().let { session ->
                return@runBlocking ApiConfig.getApiService(session.accessToken)
            }
        }
    }

    fun getReviews(params: RentHistoryPSParams): LiveData<PagingData<RentItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15
            ),
            pagingSourceFactory = {
                RentHistoryPagingSource(getApiService(), params)
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