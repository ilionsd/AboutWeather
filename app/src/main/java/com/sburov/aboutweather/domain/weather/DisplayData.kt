package com.sburov.aboutweather.domain.weather

import java.time.LocalDateTime

data class DisplayData<T>(val value: T, val unit: String) {
    override fun toString(): String = "${value}${unit}"
}

data class DisplayWeather(
    // Main data
    val time: LocalDateTime,
    val weatherType: WeatherType,
    val temperature: DisplayData<Float>,
    // Included in current weather
    val windSpeed: DisplayData<Float>?,
    val windDirection: DisplayData<Float>?,
    // Forecast
    val pressure: DisplayData<Float>?,
    val humidity: DisplayData<Float>?,
)

data class DisplayInfo(
    val current: DisplayWeather? = null,
    val forecast: List<DisplayWeather>? = null,
)
