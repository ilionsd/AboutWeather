package com.sburov.aboutweather.openweathermap

import com.sburov.aboutweather.openweathermap.data.Weather
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class OpenWeatherMapApiTest {

    companion object {
        const val HOST = "api.openweathermap.org"
        const val API_KEY_TEST = "placeholder"
        const val LAT: Float = 56.3269F
        const val LON: Float = 44.0075F
        val UNITS = Units.METRIC
        val MODE = Mode.JSON
        val LANG = Language.ENGLISH
    }

    var client: HttpClient? = null

    @BeforeEach
    fun createHttpClient() {
        client = HttpClient(CIO) {
            install(Resources)
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                }
            }
            install(ContentNegotiation) {
                json()
                xml()
            }
        }
    }

    @Test
    fun currentWeatherJsonTest() {
        val response: HttpResponse = runBlocking {
            client!!.get(OpenWeatherMap.CurrentWeather(OpenWeatherMap(API_KEY_TEST, MODE, LANG), LAT, LON, UNITS))
        }
        when (response.status.value) {
            in 200..299 -> {
                val data: Weather = runBlocking {
                    response.body()
                }
                with(data) {
                    assertNotNull(coordinates)
                    assert(weather.isNotEmpty())
                    assertNotNull(base)
                    assertNotNull(main)
                    assertNotNull(visibility)
                    assertNotNull(wind)
                    assertNotNull(clouds)
                    assertNotNull(rain)
                    assertNotNull(snow)
                    assertNotNull(dt)
                    assertNotNull(sys)
                    assertNotNull(timezone)
                    assertNotNull(cityID)
                    assertNotNull(cityName)
                    assertNotNull(code)
                }
            }
            else -> AssertionError(response.status.value)
        }
    }

}