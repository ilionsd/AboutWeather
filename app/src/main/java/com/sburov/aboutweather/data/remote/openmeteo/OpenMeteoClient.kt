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
import io.ktor.client.statement.*
import javax.inject.Inject

class OpenMeteoClient @Inject constructor()
    : WeatherDataProvider {

    companion object {
        const val REMOTE_ENDPOINT = "api.open-meteo.com"
    }

    private val client: HttpClient = HttpClientFactory(OkHttp)
        .getHttpClient(REMOTE_ENDPOINT)

    override suspend fun getForecast(location: Location): Either<ApiError, OpenMeteoData> =
        getForecast(location.latitude.toFloat(), location.longitude.toFloat())

    suspend fun getForecast(lat: Float, lon: Float): Either<ApiError, OpenMeteoData> = try {
        makeGetRequest(
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
                    Variable.WIND_DIRECTION_10M,
                    Variable.HUMIDITY_RELATIVE_2M,
                    Variable.WEATHER_CODE_WMO), true
            )
        ) .map { response ->
            response.body()
        }
    } catch (e: NoTransformationFoundException) {
        Either.Left(ApiError.UnexpectedResponse)
    }

    suspend fun getCurrentWeather(lat: Float, lon: Float): Either<ApiError, OpenMeteoData> = try {
        makeGetRequest(
            OpenMeteoRestApi.Forecast(
                OpenMeteoRestApi(
                    TemperatureUnit.C,
                    WindspeedUnit.M_S,
                    PrecipitationUnit.MM,
                    TimeFormat.ISO8601,
                    TimeZone.AUTO
                ),
                lat, lon,
                setOf(), true
            )
        ) .map { response ->
            response.body()
        }
    } catch (e: NoTransformationFoundException) {
        Either.Left(ApiError.UnexpectedResponse)
    }

    private suspend inline fun <reified T: Any> makeGetRequest(resource: T): Either<ApiError, HttpResponse> = try {
        val response: HttpResponse = client.get(resource)
        Either.Right(response)
    } catch (e: RedirectResponseException) {
        // 3xx
        Either.Left(ApiError.RedirectResponse(e.response.status.value))
    } catch (e: ClientRequestException) {
        // 4xx
        Either.Left(ApiError.ClientRequest(e.response.status.value))
    } catch (e: ServerResponseException) {
        // 5xx
        Either.Left(ApiError.ServerResponse(e.response.status.value))
    } catch (e: Exception) {
        Either.Left(ApiError.UnknownError)
    }
}