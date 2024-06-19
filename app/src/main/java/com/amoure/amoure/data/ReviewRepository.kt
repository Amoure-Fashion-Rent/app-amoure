package com.amoure.amoure.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.amoure.amoure.data.pagingsource.ReviewPSParams
import com.amoure.amoure.data.pagingsource.ReviewPagingSource
import com.amoure.amoure.data.pref.UserModel
import com.amoure.amoure.data.pref.UserPreference
import com.amoure.amoure.data.response.ReviewItem
import com.amoure.amoure.data.retrofit.ApiConfig
import com.amoure.amoure.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ReviewRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun getSession(): UserModel {
        return userPreference.getSession().first()
    }

    suspend fun getApiService(): ApiService {
        val session = getSession()
        return ApiConfig.getApiService(session.accessToken)
    }

    fun getReviews(params: ReviewPSParams): LiveData<PagingData<ReviewItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                ReviewPagingSource(runBlocking { getApiService() }, params)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: ReviewRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): ReviewRepository =
            instance ?: synchronized(this) {
                instance ?: ReviewRepository(userPreference)
            }.also { instance = it }
    }
}