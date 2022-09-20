package com.sburov.aboutweather.di

import com.sburov.aboutweather.domain.LocationProvider
import com.sburov.aboutweather.data.location.LocationReceiver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(locationReceiver: LocationReceiver) : LocationProvider
}