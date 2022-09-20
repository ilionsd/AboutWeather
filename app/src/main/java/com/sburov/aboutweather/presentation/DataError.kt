package com.sburov.aboutweather.presentation

sealed class DataError {
    object Unavailable : DataError()
    object InProgress : DataError()
}