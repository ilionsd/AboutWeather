package com.sburov.aboutweather.data.mappers

import com.sburov.aboutweather.data.remote.openmeteo.CurrentWeather
import com.sburov.aboutweather.data.remote.openmeteo.OpenMeteoData
import com.sburov.aboutweather.data.remote.openmeteo.Variable
import com.sburov.aboutweather.presentation.DisplayData
import com.sburov.aboutweather.presentation.DisplayWeather
import com.sburov.aboutweather.presentation.DisplayInfo
import com.sburov.aboutweather.presentation.WeatherType
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.LocalDateTime

fun mapToDisplay(data: Map<Variable, Array<Any?>>, units: Map<Variable, String>) : List<DisplayWeather> {
    val list = mutableListOf<DisplayWeather>()

    val dataSize = data[Variable.TIME]!!.size
    for (k in 0 until dataSize) {
        try {
            DisplayWeather(
                time = (data[Variable.TIME]!![k] as LocalDateTime).toJavaLocalDateTime(),
                weatherType = WeatherType.fromWMO(data[Variable.WEATHER_CODE_WMO]!![k] as Int),
                temperature = DisplayData((data[Variable.TEMPERATURE_2M]!![k] as Float),
                    units[Variable.TEMPERATURE_2M]!!),
                windSpeed = DisplayData((data[Variable.WIND_SPEED_10M]!![k] as Float),
                    units[Variable.WIND_SPEED_10M]!!),
                windDirection = DisplayData(data[Variable.WIND_DIRECTION_10M]!![k] as Float,
                    units[Variable.WIND_DIRECTION_10M]!!)
            )
        }
        catch (e: NullPointerException) {
            null
        } ?.let {
            list.add(it)
        }
    }
    return list
}

fun CurrentWeather.toDisplayWeather(units: Map<Variable, String>): DisplayWeather = DisplayWeather(
    time = time.toJavaLocalDateTime(),
    weatherType = weatherCodeWMO.let { WeatherType.fromWMO(it.toInt()) },
    temperature = DisplayData(temperature, units[Variable.TEMPERATURE_2M]!!),
    windSpeed = DisplayData(windSpeed, units[Variable.WIND_SPEED_10M]!!),
    windDirection = DisplayData(windDirection, units[Variable.WIND_DIRECTION_10M]!!)
)

fun OpenMeteoData.toDisplayData(): DisplayInfo {
    val current = currentWeather ?. toDisplayWeather(hourlyUnits!!)
    val forecast = hourlyData ?. let { mapToDisplay(it.data, hourlyUnits!!) }
    return DisplayInfo(current, forecast)
}
