package com.sburov.aboutweather.data.remote

sealed class ApiError {
    // 3xx
    data class RedirectResponse(val code: Int): ApiError()
    // 4xx
    data class ClientRequest(val code: Int): ApiError()
    // 5xx
    data class ServerResponse(val code: Int): ApiError()

    object UnexpectedResponse: ApiError()

    object UnknownError: ApiError()
}
