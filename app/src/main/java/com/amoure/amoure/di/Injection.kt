package com.amoure.amoure.di

import android.content.Context
import com.amoure.amoure.data.ProductRepository
import com.amoure.amoure.data.RentHistoryRepository
import com.amoure.amoure.data.Repository
import com.amoure.amoure.data.ReviewRepository
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pref.UserPreference
import com.amoure.amoure.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        return Repository(
            UserRepository.getInstance(pref),
            ProductRepository.getInstance(pref),
            ReviewRepository.getInstance(pref),
            RentHistoryRepository.getInstance(pref)
        )
    }
}