package com.sburov.aboutweather.openweathermap

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*

class OpenWeatherMapClientKtor constructor(
    httpClientEngine: HttpClientEngine? = null) {

    companion object {
        const val REMOTE_ENDPOINT = "api.openweathermap.org"
        const val API_KEY = "placeholder"
    }

    private val client: HttpClient = HttpClient(httpClientEngine!!) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = REMOTE_ENDPOINT
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }
        install(Resources)
        install(ContentNegotiation) {
            json()
            xml()
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }
    }
}