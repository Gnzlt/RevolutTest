package com.gnzlt.revoluttest.di

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideJson(): Json =
        Json(JsonConfiguration.Stable)
}
