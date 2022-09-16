package com.sburov.aboutweather.data.remote

sealed class ApiError

data class UnknownError(val code: Int) : ApiError()

object NetworkError : ApiError()
