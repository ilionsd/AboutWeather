package com.sburov.aboutweather.data.remote.openmeteo

import android.location.Location
import arrow.core.Either
import com.sburov.aboutweather.data.remote.HttpClientFactory
import com.sburov.aboutweather.data.remote.ApiError
import com.sburov.aboutweather.data.remote.NetworkError
import com.sburov.aboutweather.data.remote.UnknownError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*

class OpenMeteoClient {

    companion object {
        const val REMOTE_ENDPOINT = "api.open-meteo.com"
    }

    private val client: HttpClient = HttpClientFactory(OkHttp)
        .getHttpClient(REMOTE_ENDPOINT)

    suspend fun getForecast(location: Location): Either<ApiError, OpenMeteoData> =
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
                lat, lon, true
            )
        )
        val data: OpenMeteoData = response.body()
        Either.Right(data)
    } catch (e: RedirectResponseException) {
        // 3xx
        Either.Left(UnknownError(e.response.status.value))
    } catch (e: ClientRequestException) {
        // 4xx
        Either.Left(UnknownError(e.response.status.value))
    } catch (e: ServerResponseException) {
        // 5xx
        Either.Left(UnknownError(e.response.status.value))
    } catch (e: Exception) {
        Either.Left(NetworkError)
    }
}