package com.sburov.aboutweather.data.remote.openmeteo

import com.sburov.aboutweather.data.remote.HttpClientFactory
import com.sburov.aboutweather.openweathermap.data.OpenWeatherMapData

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class OpenMeteoApiTest {

    companion object {
        const val REMOTE_ENDPOINT = "api.open-meteo.com"
        const val LAT: Float = 56.3269F
        const val LON: Float = 44.0075F
    }

    var client: HttpClient? = null

    @BeforeEach
    fun createHttpClient() {
        client = HttpClientFactory(OkHttp).getHttpClient(REMOTE_ENDPOINT)
    }

    @Test
    fun forecastTest() {
        val response: HttpResponse = runBlocking {
            client!!.get(
                OpenMeteoRestApi.Forecast(
                    OpenMeteoRestApi(
                        TemperatureUnit.C,
                        WindspeedUnit.M_S,
                        PrecipitationUnit.MM,
                        TimeFormat.ISO8601,
                        TimeZone.AUTO),
                    LAT, LON,
                    setOf(
                        Variable.TEMPERATURE_2M,
                        Variable.PRESSURE_SURFACE,
                        Variable.WIND_SPEED_10M,
                        Variable.HUMIDITY_RELATIVE_2M,
                        Variable.WEATHER_CODE_WMO),true
                )
            )
        }
        when (response.status.value) {
            in 200..299 -> {
                val data: OpenMeteoData = runBlocking {
                    response.body()
                }
                with (data) {
                    assertNotNull(currentWeather)
                    assertNotNull(hourlyUnits)
                    assertNotNull(hourlyData)
                }
            }
            else -> AssertionError(response.status.value)
        }
    }
}