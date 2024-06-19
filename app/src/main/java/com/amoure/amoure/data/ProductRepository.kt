package com.amoure.amoure.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.amoure.amoure.data.pagingsource.ProductPSParams
import com.amoure.amoure.data.pagingsource.ProductPagingSource
import com.amoure.amoure.data.pref.UserModel
import com.amoure.amoure.data.pref.UserPreference
import com.amoure.amoure.data.response.ProductItem
import com.amoure.amoure.data.retrofit.ApiConfig
import com.amoure.amoure.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProductRepository private constructor(
    private val userPreference: UserPreference
) {
    suspend fun getSession(): UserModel {
        return userPreference.getSession().first()
    }

    suspend fun getApiService(): ApiService {
        val session = getSession()
        return ApiConfig.getApiService(session.accessToken)
    }

    fun getProducts(apiService: ApiService?, params: ProductPSParams): LiveData<PagingData<ProductItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                ProductPagingSource(runBlocking { getApiService() }, params)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: ProductRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): ProductRepository =
            instance ?: synchronized(this) {
                instance ?: ProductRepository(userPreference)
            }.also { instance = it }
    }
}