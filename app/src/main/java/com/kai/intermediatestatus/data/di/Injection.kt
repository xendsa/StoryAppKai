package com.kai.intermediatestatus.data.di

import android.content.Context
import com.kai.intermediatestatus.data.Repository
import com.kai.intermediatestatus.data.api.ApiConfig
import com.kai.intermediatestatus.data.local.UserPreference
import com.kai.intermediatestatus.data.local.dataStore

object Injection {

    fun provideRepository(context: Context): Repository {
        val preference = UserPreference.getInstance(context.dataStore)
        val service = ApiConfig.getApiService()
        return Repository.getInstance(preference, service)
    }
}