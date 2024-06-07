package com.amoure.amoure.di

import android.content.Context
import com.amoure.amoure.data.UserRepository
import com.amoure.amoure.data.pref.UserPreference
import com.amoure.amoure.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}