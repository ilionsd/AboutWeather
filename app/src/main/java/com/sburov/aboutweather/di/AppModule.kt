package com.sburov.aboutweather.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sburov.aboutweather.domain.LocationProvider
import com.sburov.aboutweather.data.location.LocationReceiver
import com.sburov.aboutweather.domain.WeatherDataProvider
import com.sburov.aboutweather.data.remote.openmeteo.OpenMeteoClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }
}