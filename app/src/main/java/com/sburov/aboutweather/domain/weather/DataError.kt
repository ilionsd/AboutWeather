package com.sburov.aboutweather.domain.weather

sealed class DataError {
    object GeolocationUnavailable: DataError()
    object WeatherInfoUnavailable : DataError()
    object InProgress : DataError()
}