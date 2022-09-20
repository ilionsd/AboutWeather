package com.sburov.aboutweather.data.mappers

import com.sburov.aboutweather.data.remote.openmeteo.CurrentWeather
import com.sburov.aboutweather.data.remote.openmeteo.Measurements
import com.sburov.aboutweather.data.remote.openmeteo.OpenMeteoData
import com.sburov.aboutweather.data.remote.openmeteo.Variable
import com.sburov.aboutweather.presentation.Measurement
import com.sburov.aboutweather.presentation.WeatherData
import com.sburov.aboutweather.presentation.WeatherInfo
import com.sburov.aboutweather.presentation.WeatherType
import kotlinx.datetime.toJavaLocalDateTime

fun Measurements.toWeatherDataList(units: Map<Variable, String>) : List<WeatherData> {
    val list = mutableListOf<WeatherData>()
    for (k in time.indices) {
        try {
            WeatherData(
                time = time[k].toJavaLocalDateTime(),
                weatherType = measurements[Variable.WEATHER_CODE_WMO]?.let {
                    WeatherType.fromWMO(it[k].toInt())
                }!!,
                temperature = measurements[Variable.TEMPERATURE_2M]?.let {
                    Measurement(it[k], units[Variable.TEMPERATURE_2M]!!)
                }!!,
                windSpeed = measurements[Variable.WIND_SPEED_10M]?.let {
                    Measurement(it[k], units[Variable.WIND_SPEED_10M]!!)
                }!!,
                windDirection = measurements[Variable.WIND_DIRECTION_10M]?.let {
                    Measurement(it[k], units[Variable.WIND_DIRECTION_10M]!!)
                }!!
            )
        } catch (e: NullPointerException) {
            null
        } ?.let {
            list.add(it)
        }
    }
    return list
}

fun CurrentWeather.toWeatherData(units: Map<Variable, String>): WeatherData = WeatherData(
    time = time.toJavaLocalDateTime(),
    weatherType = weatherCodeWMO.let { WeatherType.fromWMO(it.toInt()) },
    temperature = Measurement(temperature, units[Variable.TEMPERATURE_2M]!!),
    windSpeed = Measurement(windSpeed, units[Variable.WIND_SPEED_10M]!!),
    windDirection = Measurement(windDirection, units[Variable.WIND_DIRECTION_10M]!!)
)

fun OpenMeteoData.toWeatherInfo() : WeatherInfo {
    val current = currentWeather?.toWeatherData(hourlyUnits!!)
    val forecast = hourlyData?.toWeatherDataList(hourlyUnits!!)
    return WeatherInfo(current, forecast)
}
