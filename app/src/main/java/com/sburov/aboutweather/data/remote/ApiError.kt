package com.sburov.aboutweather.data.remote

sealed class ApiError {
    object NetworkError: ApiError()
    data class UnknownError(val code: Int): ApiError()
}
