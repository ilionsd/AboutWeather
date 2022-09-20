package com.sburov.aboutweather.data.remote.openmeteo

import android.location.Location
import arrow.core.Either
import com.sburov.aboutweather.data.remote.*
import com.sburov.aboutweather.domain.WeatherDataProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*

class OpenMeteoClient : WeatherDataProvider {

    companion object {
        const val REMOTE_ENDPOINT = "api.open-meteo.com"
    }

    private val client: HttpClient = HttpClientFactory(OkHttp)
        .getHttpClient(REMOTE_ENDPOINT)

    override suspend fun getForecast(location: Location): Either<ApiError, OpenMeteoData> =
        getForecast(location.latitude.toFloat(), location.longitude.toFloat())

    suspend fun getForecast(lat: Float, lon: Float) : Either<ApiError, OpenMeteoData> = try {
        val response = client.get(
            OpenMeteoRestApi.Forecast(
                OpenMeteoRestApi(
                    TemperatureUnit.C,
                    WindspeedUnit.M_S,
                    PrecipitationUnit.MM,
                    TimeFormat.ISO8601,
                    TimeZone.AUTO
                ),
                lat, lon,
                setOf(
                    Variable.TEMPERATURE_2M,
                    Variable.PRESSURE_SURFACE,
                    Variable.WIND_SPEED_10M,
                    Variable.HUMIDITY_RELATIVE_2M,
                    Variable.WEATHER_CODE_WMO), true
            )
        )
        val data: OpenMeteoData = response.body()
        Either.Right(data)
    } catch (e: RedirectResponseException) {
        // 3xx
        Either.Left(ApiError.UnknownError(e.response.status.value))
    } catch (e: ClientRequestException) {
        // 4xx
        Either.Left(ApiError.UnknownError(e.response.status.value))
    } catch (e: ServerResponseException) {
        // 5xx
        Either.Left(ApiError.UnknownError(e.response.status.value))
    } catch (e: Exception) {
        Either.Left(ApiError.NetworkError)
    }
}