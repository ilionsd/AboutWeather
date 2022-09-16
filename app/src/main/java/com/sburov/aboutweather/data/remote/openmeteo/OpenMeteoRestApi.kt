package com.sburov.aboutweather.data.remote.openmeteo

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TemperatureUnit {
    @SerialName("celsius")
    C,
    @SerialName("fahrenheit")
    F
}

@Serializable
enum class WindspeedUnit {
    @SerialName("kmh")
    KM_H,
    @SerialName("ms")
    M_S,
    @SerialName("mph")
    MPH,
    @SerialName("kn")
    KN
}

@Serializable
enum class PrecipitationUnit {
    @SerialName("mm")
    MM,
    @SerialName("inch")
    INCH
}

@Serializable
enum class TimeFormat {
    @SerialName("iso8601")
    ISO8601,
    @SerialName("unixtime")
    UNIXTIME
}

@Serializable
enum class TimeZone {
    @SerialName("auto")
    AUTO
}

@Serializable
@Resource("/v1")
class OpenMeteoRestApi(
    @SerialName("temperature_unit")
    val temperatureUnit: TemperatureUnit,

    @SerialName("windspeed_unit")
    val windspeedUnit: WindspeedUnit,

    @SerialName("precipitation_unit")
    val precipitationUnit: PrecipitationUnit,

    @SerialName("timeformat")
    val timeFormat: TimeFormat,

    @SerialName("timezone")
    val timeZone: TimeZone
) {
    @Serializable
    @Resource("/forecast")
    class Forecast(
        val parent: OpenMeteoRestApi,

        @SerialName("latitude")
        val lat: Float,

        @SerialName("longitude")
        val lon: Float,

        @SerialName("current_weather")
        val current: Boolean,
    )
}

