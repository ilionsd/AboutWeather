package com.sburov.aboutweather.data

import android.location.Location
import kotlinx.serialization.Contextual
import java.time.Instant
import java.time.ZoneOffset

@kotlinx.serialization.Serializable
data class WeatherDataXml(
    val city: City?,
    val temperature: IntervalMeasurement?,
    val feelsLike: Measurement?,
    val humidity: Measurement?,
    val pressure: Measurement?,
    val wind: Wind?,
    val clouds: Clouds?,
    val visibility: Measurement?,

)

@kotlinx.serialization.Serializable
data class City(
    val id: Int?,
    val name: String?,
    @Contextual
    val coordinates: Location?,
    val country: String?,
    @Contextual
    val timezone: ZoneOffset?,
    val sun: Sun?,
)

@kotlinx.serialization.Serializable
data class Sun(
    @Contextual
    val rise: Instant?,
    @Contextual
    val set: Instant?,
)

@kotlinx.serialization.Serializable
data class Measurement(
    val value: Float?,
    val name: String?,
    val unit: String?,
)

@kotlinx.serialization.Serializable
data class IntervalMeasurement(
    val value: Float?,
    val max: Float?,
    val min: Float?,
    val unit: String?,
)

@kotlinx.serialization.Serializable
data class Wind(
    val speed: Speed?,
    val gust: Speed?,
    val direction: Direction?,
)

@kotlinx.serialization.Serializable
data class Speed(
    val value: Float?,
    val name: String?,
    val unit: String?,
)

@kotlinx.serialization.Serializable
data class Direction(
    val value: Float?,
    val name: String?,
    val code: String?,
)

