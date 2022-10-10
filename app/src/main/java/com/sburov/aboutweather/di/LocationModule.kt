package com.sburov.aboutweather.di

import com.sburov.aboutweather.data.location.LocationProviderImpl
import com.sburov.aboutweather.data.location.LocationReceiverImpl
import com.sburov.aboutweather.domain.LocationProvider
import com.sburov.aboutweather.domain.LocationReceiver
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
    abstract fun bindLocationProvider(locationProvider: LocationProviderImpl) : LocationProvider

    @Binds
    @Singleton
    abstract fun bindLocationReceiver(locationReceiver: LocationReceiverImpl) : LocationReceiver
}
