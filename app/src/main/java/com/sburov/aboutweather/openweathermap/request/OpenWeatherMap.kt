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

enum class Language(name: String) {
    AFRIKAANS("af"),
    ALBANIAN("al"),
    ARABIC("ar"),
    AZERBAIJANI("az"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CZECH("cz"),
    DANISH("da"),
    GERMAN("de"),
    GREEK("el"),
    ENGLISH("en"),
    BASQUE("eu"),
    PERSIAN("fa"),
    FINNISH("fi"),
    FRENCH("fr"),
    GALICIAN("gl"),
    HEBREW("he"),
    HINDI("hi"),
    CROATIAN("hr"),
    HUNGARIAN("hu"),
    INDONESIAN("id"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("kr"),
    LATVIAN("la"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    NORWEGIAN("no"),
    DUTCH("nl"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    PORTUGUES_BRASIL("pt_br"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SWEDISH("sv"), // sv, se
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("sp"), // sp, es
    SERBIAN("sr"),
    THAI("th"),
    TURKISH("tr"),
    UKRAINIAN("ua"), // ua, uk
    VIETNAMESE("vi"),
    CHINESE_SIMPLIFIED("zh_cn"),
    CHINESE_TRADITIONAL("zh_tw"),
    ZULU("zu"),
}

@Serializable
@Resource("/data/2.5")
class OpenWeatherMap constructor(
    val appID: String,
    val mode: Mode,
    val lang: Language = Language.ENGLISH,
) {
    @Serializable
    @Resource("/weather")
    class CurrentWeather constructor(
        val parent: OpenWeatherMap,
        val lat: Float,
        val lon: Float,
        val units: Units = Units.METRIC,
    )
}