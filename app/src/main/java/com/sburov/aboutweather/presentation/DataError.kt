package com.sburov.aboutweather.presentation

sealed class DataError {
    object GeolocationUnavailable: DataError()
    object WeatherInfoUnavailable : DataError()
    object InProgress : DataError()
}