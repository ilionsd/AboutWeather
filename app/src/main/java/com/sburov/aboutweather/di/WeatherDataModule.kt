package com.sburov.aboutweather.di

import com.sburov.aboutweather.domain.WeatherDataProvider
import com.sburov.aboutweather.data.remote.openmeteo.OpenMeteoClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherDataModule {

    @Binds
    @Singleton
    abstract fun bindWeatherDataProvider(openMeteoClient: OpenMeteoClient): WeatherDataProvider
}
