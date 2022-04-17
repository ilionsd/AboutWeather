package com.sburov.aboutweather.openweathermap

import io.ktor.resources.*
import kotlinx.serialization.*

enum class Units(name: String) {
    STANDARD("standard"),
    METRIC("metric"),
    IMPERIAL("imperial"),
}

enum class Mode(name: String) {
    JSON("json"),
    XML("xml"),
    HTML("html"),
}

@Serializable
@Resource("/data/2.5")
class OpenWeatherMap constructor(
    val appID: String,
    val mode: Mode
) {
    @Serializable
    @Resource("/weather")
    class CurrentWeather constructor(
        val parent: OpenWeatherMap,
        val lat: Float,
        val lon: Float,
        val units: Units,
    )
}