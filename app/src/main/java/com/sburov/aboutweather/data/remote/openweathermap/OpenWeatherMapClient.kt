package com.sburov.aboutweather.data.remote.openweathermap

import android.location.Location
import arrow.core.Either
import com.sburov.aboutweather.data.remote.ApiError
import com.sburov.aboutweather.openweathermap.*
import com.sburov.aboutweather.openweathermap.data.OpenWeatherMapData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*

class OpenWeatherMapClient(
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

    suspend fun getCurrentWeather(location: Location, units: Units = Units.METRIC) : Either<ApiError, OpenWeatherMapData> =
        getCurrentWeather(location.latitude.toFloat(), location.longitude.toFloat(), units)

    suspend fun getCurrentWeather(lat: Float, lon: Float, units: Units = Units.METRIC) : Either<ApiError, OpenWeatherMapData> = try {
        val response: HttpResponse = client.get(
            OpenWeatherMapRestApi.CurrentWeather(
                OpenWeatherMapRestApi(API_KEY, Mode.JSON, Language.ENGLISH),
                lat, lon, units
            )
        )
        val data: OpenWeatherMapData = response.body()
        Either.Right(data)
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
