package com.sburov.aboutweather.domain

import android.location.Location
import arrow.core.Either
import com.sburov.aboutweather.data.remote.ApiError
import com.sburov.aboutweather.data.remote.openmeteo.OpenMeteoData

interface WeatherDataProvider {
    suspend fun getForecast(location: Location): Either<ApiError, OpenMeteoData>
}