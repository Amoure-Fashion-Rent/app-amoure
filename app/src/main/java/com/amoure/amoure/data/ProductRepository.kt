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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProductRepository private constructor(
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

    fun getProducts(params: ProductPSParams): LiveData<PagingData<ProductItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                ProductPagingSource(getApiService(), params)
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