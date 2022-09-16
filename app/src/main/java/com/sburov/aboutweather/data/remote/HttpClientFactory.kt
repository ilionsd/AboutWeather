package com.sburov.aboutweather.data.remote

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*

class HttpClientFactory<T : HttpClientEngineConfig>(
    private val httpClientEngineFactory: HttpClientEngineFactory<T>? = null
) {
    fun getHttpClient(remoteEndpoint: String) : HttpClient = HttpClient(httpClientEngineFactory!!) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = remoteEndpoint
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