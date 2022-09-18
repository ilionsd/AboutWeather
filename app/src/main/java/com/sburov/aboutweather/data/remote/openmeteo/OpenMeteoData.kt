package com.sburov.aboutweather.data.remote.openmeteo

import androidx.annotation.DrawableRes
import com.sburov.aboutweather.R
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

sealed class WeatherType(
    val weatherDesc: String,
    @DrawableRes val iconRes: Int
) {
    object ClearSky : WeatherType(
        weatherDesc = "Clear sky",
        iconRes = R.drawable.ic_sunny
    )

    object MainlyClear : WeatherType(
        weatherDesc = "Mainly clear",
        iconRes = R.drawable.ic_cloudy
    )

    object PartlyCloudy : WeatherType(
        weatherDesc = "Partly cloudy",
        iconRes = R.drawable.ic_cloudy
    )

    object Overcast : WeatherType(
        weatherDesc = "Overcast",
        iconRes = R.drawable.ic_cloudy
    )

    object Foggy : WeatherType(
        weatherDesc = "Foggy",
        iconRes = R.drawable.ic_very_cloudy
    )

    object DepositingRimeFog : WeatherType(
        weatherDesc = "Depositing rime fog",
        iconRes = R.drawable.ic_very_cloudy
    )

    object LightDrizzle : WeatherType(
        weatherDesc = "Light drizzle",
        iconRes = R.drawable.ic_rainshower
    )

    object ModerateDrizzle : WeatherType(
        weatherDesc = "Moderate drizzle",
        iconRes = R.drawable.ic_rainshower
    )

    object DenseDrizzle : WeatherType(
        weatherDesc = "Dense drizzle",
        iconRes = R.drawable.ic_rainshower
    )

    object LightFreezingDrizzle : WeatherType(
        weatherDesc = "Slight freezing drizzle",
        iconRes = R.drawable.ic_snowyrainy
    )

    object DenseFreezingDrizzle : WeatherType(
        weatherDesc = "Dense freezing drizzle",
        iconRes = R.drawable.ic_snowyrainy
    )

    object SlightRain : WeatherType(
        weatherDesc = "Slight rain",
        iconRes = R.drawable.ic_rainy
    )

    object ModerateRain : WeatherType(
        weatherDesc = "Rainy",
        iconRes = R.drawable.ic_rainy
    )

    object HeavyRain : WeatherType(
        weatherDesc = "Heavy rain",
        iconRes = R.drawable.ic_rainy
    )

    object HeavyFreezingRain : WeatherType(
        weatherDesc = "Heavy freezing rain",
        iconRes = R.drawable.ic_snowyrainy
    )

    object SlightSnowFall : WeatherType(
        weatherDesc = "Slight snow fall",
        iconRes = R.drawable.ic_snowy
    )

    object ModerateSnowFall : WeatherType(
        weatherDesc = "Moderate snow fall",
        iconRes = R.drawable.ic_heavysnow
    )

    object HeavySnowFall : WeatherType(
        weatherDesc = "Heavy snow fall",
        iconRes = R.drawable.ic_heavysnow
    )

    object SnowGrains : WeatherType(
        weatherDesc = "Snow grains",
        iconRes = R.drawable.ic_heavysnow
    )

    object SlightRainShowers : WeatherType(
        weatherDesc = "Slight rain showers",
        iconRes = R.drawable.ic_rainshower
    )

    object ModerateRainShowers : WeatherType(
        weatherDesc = "Moderate rain showers",
        iconRes = R.drawable.ic_rainshower
    )

    object ViolentRainShowers : WeatherType(
        weatherDesc = "Violent rain showers",
        iconRes = R.drawable.ic_rainshower
    )

    object SlightSnowShowers : WeatherType(
        weatherDesc = "Light snow showers",
        iconRes = R.drawable.ic_snowy
    )

    object HeavySnowShowers : WeatherType(
        weatherDesc = "Heavy snow showers",
        iconRes = R.drawable.ic_snowy
    )

    object ModerateThunderstorm : WeatherType(
        weatherDesc = "Moderate thunderstorm",
        iconRes = R.drawable.ic_thunder
    )

    object SlightHailThunderstorm : WeatherType(
        weatherDesc = "Thunderstorm with slight hail",
        iconRes = R.drawable.ic_rainythunder
    )

    object HeavyHailThunderstorm : WeatherType(
        weatherDesc = "Thunderstorm with heavy hail",
        iconRes = R.drawable.ic_rainythunder
    )

    companion object {
        fun fromWMO(code: Int): WeatherType {
            return when (code) {
                0 -> ClearSky
                1 -> MainlyClear
                2 -> PartlyCloudy
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSky
            }
        }
    }
}

@Serializable
enum class Variable {
    @SerialName("time")
    TIME,

    @SerialName("temperature_2m")
    TEMPERATURE_2M,
    @SerialName("apparent_temperature")
    TEMPERATURE_APPARENT,

    @SerialName("relativehumidity_2m")
    HUMIDITY_RELATIVE_2M,
    @SerialName("dewpoint_2m")
    DEW_POINT_2M,

    @SerialName("pressure_msl")
    PRESSURE_MEAN_SEA_LEVEL,
    @SerialName("surface_pressure")
    PRESSURE_SURFACE,

    @SerialName("cloudcover")
    CLOUD_COVER,
    @SerialName("cloudcover_low")
    CLOUD_COVER_LOW,
    @SerialName("cloudcover_mid")
    CLOUD_COVER_MID,
    @SerialName("cloudcover_high")
    CLOUD_COVER_HIGH,

    @SerialName("windspeed_10m")
    WIND_SPEED_10M,
    @SerialName("windspeed_80m")
    WIND_SPEED_80M,
    @SerialName("windspeed_120m")
    WIND_SPEED_120M,
    @SerialName("windspeed_180m")
    WIND_SPEED_180M,

    @SerialName("windgusts_10m")
    WIND_GUST_10M,

    @SerialName("shortwave_radiation")
    RADIATION_SHORTWAVE,
    @SerialName("direct_radiation")
    RADIATION_DIRECT,
    @SerialName("direct_normal_irradiance")
    RADIATION_DIRECT_NORMAL,
    @SerialName("diffuse_radiation")
    RADIATION_DIFFUSE,

    @SerialName("vapor_pressure_deficit")
    VAPOR_PRESSURE_DEFICIT,
    @SerialName("evapotranspiration")
    EVAPOTRANSPIRATION,
    @SerialName("et0_fao_evapotranspiration")
    EVAPOTRANSPIRATION_ET0_FAO,

    @SerialName("precipitation")
    PRECIPITATION_TOTAL,
    @SerialName("snowfall")
    PRECIPITATION_SNOWFALL,
    @SerialName("rain")
    PRECIPITATION_RAIN,
    @SerialName("showers")
    PRECIPITATION_SHOWERS,

    @SerialName("weathercode")
    WEATHER_CODE_WMO,

    @SerialName("snow_depth")
    SNOW_DEPTH,

    @SerialName("freezinglevel_height")
    FREEZING_LEVEL_HEIGHT,

    @SerialName("soil_temperature_0cm")
    SOIL_TEMPERATURE_0CM,
    @SerialName("soil_temperature_6cm")
    SOIL_TEMPERATURE_6CM,
    @SerialName("soil_temperature_18cm")
    SOIL_TEMPERATURE_18CM,
    @SerialName("soil_temperature_54cm")
    SOIL_TEMPERATURE_54CM,

    @SerialName("soil_moisture_0_1cm")
    SOIL_MOISTURE_0_1CM,
    @SerialName("soil_moisture_1_3cm")
    SOIL_MOISTURE_1_3CM,
    @SerialName("soil_moisture_3_9cm")
    SOIL_MOISTURE_3_9CM,
    @SerialName("soil_moisture_9_27cm")
    SOIL_MOISTURE_9_27CM,
    @SerialName("soil_moisture_27_81cm")
    SOIL_MOISTURE_27_81CM,
}

@Serializable(with = MeasurementsSerializer::class)
data class Measurements(
    val time: Array<LocalDateTime>,
    val measurements: Map<Variable, Array<Float>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Measurements

        if (!time.contentEquals(other.time)) return false
        if (measurements != other.measurements) return false

        return true
    }

    override fun hashCode(): Int {
        var result = time.contentHashCode()
        result = 31 * result + measurements.hashCode()
        return result
    }
}

object MeasurementsSerializer : KSerializer<Measurements> {

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildSerialDescriptor("MeasurementsSerializer", PolymorphicKind.SEALED)

    override fun serialize(encoder: Encoder, value: Measurements) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Measurements = with(decoder as JsonDecoder) {
        var time = emptyArray<LocalDateTime>()
        var vars = mutableMapOf<Variable, Array<Float>>()

        val jsonObject = decodeJsonElement().jsonObject

        for (entry in jsonObject.entries) {
            val variable: Variable = Variable.values().find {
                Variable::class.java.getField(it.name).getAnnotation(SerialName::class.java)?.value == entry.key }!!
            when (variable) {
                Variable.TIME -> time = json.decodeFromJsonElement(ArraySerializer(LocalDateTime.serializer()), entry.value)
                else -> {
                    if (vars.containsKey(variable)) {
                        error("Variable $variable already exists!")
                    }
                    vars[variable] = json.decodeFromJsonElement(ArraySerializer(Float.serializer()), entry.value)
                }
            }
        }
        Measurements(time, vars)
    }
}

@Serializable
data class OpenMeteoData(
    @SerialName("latitude")
    val lat: Float,

    @SerialName("longitude")
    val lon: Float,

    val elevation: Float,

    @SerialName("generationtime_ms")
    val generationTime: Float,

    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Long,

    @SerialName("timezone")
    val timeZone: String,

    @SerialName("timezone_abbreviation")
    val timeZoneAbbreviation: String,

    @SerialName("current_weather")
    val currentWeather: CurrentWeather? = null,

    @SerialName("hourly_units")
    val hourlyUnits: Map<Variable, String>? = null,

    @SerialName("hourly")
    val hourlyData: Measurements? = null,
)

@Serializable
data class CurrentWeather(
    val time: LocalDateTime,
    val temperature: Float,
    @SerialName("weathercode")
    val weatherCodeWMO: Float,
    @SerialName("windspeed")
    val windSpeed: Float,
    @SerialName("winddirection")
    val windDirection: Float,
)

