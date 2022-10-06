package com.sburov.aboutweather.openweathermap.data

import android.location.Location
import kotlinx.datetime.Instant
import kotlinx.serialization.*
import java.time.ZoneOffset

@Serializable
data class OpenWeatherMapData(
    @SerialName("coord")
    val coordinates: Coordinates?,

    @SerialName("weather")
    val weather: List<Sky?>,

    val base: String?,

    @SerialName("main")
    val main: Data?,

    val visibility: Float?,

    val wind: Wind?,

    val clouds: Clouds?,

    val rain: Precipitation?,

    val snow: Precipitation?,

    val dt: Instant?,

    @SerialName("sys")
    val sys: SystemData?,

    val timezone: Int?,

    @SerialName("id")
    val cityID: Int?,

    @SerialName("name")
    val cityName: String?,

    @SerialName("cod")
    val code: Int?,
)

@Serializable
data class Coordinates(
    val lat: Float,
    val lon: Float,
) {
    constructor(location: Location) : this(location.latitude.toFloat(), location.longitude.toFloat())
}

@Serializable
data class Sky(
    val id: WeatherCode?,
    val main: String?,
    val description: String?,
    val icon: String?,
)

@Serializable
data class Data(
    @SerialName("temp")
    val temperature: Float?,

    @SerialName("feels_like")
    val feelsLike: Float?,

    @SerialName("temp_min")
    val temperatureMin: Float?,

    @SerialName("temp_max")
    val temperatureMax: Float?,

    val pressure: Float?,

    @SerialName("sea_level")
    val pressureSeaLevel: Float?,

    @SerialName("grnd_level")
    val pressureGroundLevel: Float?,

    val humidity: Float?,
)

@Serializable
data class Wind(
    val speed: Float?,

    @SerialName("deg")
    val degree: Int?,

    val gust: Float?,
)

@Serializable
data class Clouds(
    val all: Int?
)

@Serializable
data class Precipitation(
    @SerialName("1h")
    val in1h: Float?,

    @SerialName("3h")
    val in3h: Float?,
)

@Serializable
data class SystemData(
    val type: Int?,
    val id: Int?,
    val message: Double?,
    val country: String?,
    val sunrise: Instant?,
    val sunset: Instant?,
)

@Serializable
data class XmlWeather constructor(
    val city: City?,
    val temperature: IntervalMeasurement?,
    val feelsLike: Measurement?,
    val humidity: Measurement?,
    val pressure: Measurement?,
    val wind: Wind?,
    val clouds: Clouds?,
    val visibility: Measurement?,
)

data class XmlCity(
    val id: Int?,
    val name: String?,
    val coordinates: Location?,
    val country: String?,
    val timezone: ZoneOffset?,
    val sun: Sun?,
)

data class XmlSun(
    val rise: Instant?,
    val set: Instant?,
)



@Serializable
enum class WeatherCode(code: Int) {
    // 2xx codes
    THUNDERSTORM_WITH_LIGHT_RAIN(200),
    THUNDERSTORM_WITH_RAIN(201),
    THUNDERSTORM_WITH_HEAVY_RAIN(202),
    // ...
    THUNDERSTORM_LIGHT(210),
    THUNDERSTORM(211),
    THUNDERSTORM_HEAVY(212),
    // ...
    THUNDERSTORM_RAGGED(221),
    // ...
    THUNDERSTORM_WITH_LIGHT_DRIZZLE(230),
    THUNDERSTORM_WITH_DRIZZLE(231),
    THUNDERSTORM_WITH_HEAVY_DRIZZLE(232),
    // 3xx codes
    DRIZZLE_INTENSITY_LIGHT(300),
    DRIZZLE(301),
    DRIZZLE_INTENSITY_HEAVY(302),
    // ...
    DRIZZLE_RAIN_INTENSITY_LIGHT(310),
    DRIZZLE_RAIN(311),
    DRIZZLE_RAIN_INTENSITY_HEAVY(312),
    DRIZZLE_RAIN_SHOWER(313),
    DRIZZLE_RAIN_SHOWER_HEAVY(314),
    // ...
    DRIZZLE_SHOWER(321),
    // 5xx codes
    RAIN_LIGHT(500),
    RAIN_MODERATE(501),
    RAIN_HEAVY(502),
    RAIN_VERY_HEAVY(503),
    RAIN_EXTREME(504),
    // ...
    RAIN_FREEZING(511),
    //...
    RAIN_SHOWER_LIGHT(520),
    RAIN_SHOWER(521),
    RAIN_SHOWER_HEAVY(522),
    // ...
    RAIN_SHOWER_RAGGED(531),
    // 6xx codes
    SNOW_LIGHT(600),
    SNOW(601),
    SNOW_HEAVY(602),
    // ...
    SLEET(611),
    SLEET_SHOWER_LIGHT(612),
    SLEET_SHOWER(613),
    // ...
    SNOW_RAIN_LIGHT(615),
    SNOW_RAIN(616),
    // ...
    SNOW_SHOWER_LIGHT(620),
    SNOW_SHOWER(621),
    SNOW_SHOWER_HEAVY(622),
    // 7xx codes
    MIST(701),
    // ...
    SMOKE(711),
    // ...
    HAZE(721),
    // ...
    DUST_SAND_WHIRLS(731),
    // ...
    FOG(741),
    // ...
    SAND(751),
    // ...
    DUST(761),
    ASH(762),
    // ...
    SQUALLS(771),
    // ...
    TORNADO(781),
    // 8xx codes
    CLOUDS_SKY_CLEAR(800),
    CLOUDS_FEW(801),
    CLOUDS_SCATTERED(802),
    CLOUDS_BROKEN(803),
    CLOUDS_OVERCAST(804),
}
