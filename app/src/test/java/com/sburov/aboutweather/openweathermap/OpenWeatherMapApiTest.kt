package com.sburov.aboutweather.openweathermap

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class OpenWeatherMapApiTest {

    companion object {
        const val HOST = "api.openweathermap.org"
        const val API_KEY_TEST = "placeholder"
        const val LAT: Float = 56.3269F
        const val LON: Float = 44.0075F
        val UNITS = Units.METRIC
        val MODE = Mode.JSON
    }

    @Test
    fun currentWeatherJsonTest() {
        val client = HttpClient(CIO) {
            install(Resources)
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                }
            }
        }

        val response: HttpResponse = runBlocking {
            client.get(OpenWeatherMap.CurrentWeather(OpenWeatherMap(API_KEY_TEST, MODE), LAT, LON, UNITS))
        }
        when (response.status.value) {
            in 200..299 -> print(runBlocking { response.bodyAsText() })
            else -> print(response.status)
        }
    }

}