package com.sburov.aboutweather.openweathermap

sealed class ApiError

data class UnknownError(val code: Int) : ApiError()

object NetworkError : ApiError()
