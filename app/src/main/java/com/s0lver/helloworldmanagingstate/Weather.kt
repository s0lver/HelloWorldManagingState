package com.s0lver.helloworldmanagingstate

enum class Weather(val description: String) {
    CLEAR_SKY("Clear sky"),
    MAINLY_CLEAR("Mainly clear"),
    PARTLY_CLOUDY("Partly cloudy"),
    OVERCAST("Overcast"),
    FOG("Fog"),
    DEPOSITING_RIME_FOG("Depositing rime fog"),
    DRIZZLE_LIGHT("Drizzle: Light intensity"),
    DRIZZLE_MODERATE("Drizzle: Moderate intensity"),
    DRIZZLE_DENSE("Drizzle: Dense intensity"),
    FREEZING_DRIZZLE_LIGHT("Freezing Drizzle: Light intensity"),
    FREEZING_DRIZZLE_DENSE("Freezing Drizzle: Dense intensity"),
    RAIN_SLIGHT("Rain: Slight intensity"),
    RAIN_MODERATE("Rain: Moderate intensity"),
    RAIN_HEAVY("Rain: Heavy intensity"),
    FREEZING_RAIN_LIGHT("Freezing Rain: Light intensity"),
    FREEZING_RAIN_HEAVY("Freezing Rain: Heavy intensity"),
    SNOW_FALL_SLIGHT("Snow fall: Slight intensity"),
    SNOW_FALL_MODERATE("Snow fall: Moderate intensity"),
    SNOW_FALL_HEAVY("Snow fall: Heavy intensity"),
    SNOW_GRAINS("Snow grains"),
    RAIN_SHOWERS_SLIGHT("Rain showers: Slight"),
    RAIN_SHOWERS_MODERATE("Rain showers: Moderate"),
    RAIN_SHOWERS_VIOLENT("Rain showers: Violent"),
    SNOW_SHOWERS_SLIGHT("Snow showers: Slight"),
    SNOW_SHOWERS_HEAVY("Snow showers: Heavy"),
    THUNDERSTORM("Thunderstorm: Slight or moderate"),
    THUNDERSTORM_HAIL_SLIGHT("Thunderstorm with slight hail"),
    THUNDERSTORM_HAIL_HEAVY("Thunderstorm with heavy hail"),
    UNKNOWN("Unknown");

    companion object {
        fun fromCode(code: Int): Weather {
            return when (code) {
                0 -> CLEAR_SKY
                1 -> MAINLY_CLEAR
                2 -> PARTLY_CLOUDY
                3 -> OVERCAST
                45 -> FOG
                48 -> DEPOSITING_RIME_FOG
                51 -> DRIZZLE_LIGHT
                53 -> DRIZZLE_MODERATE
                55 -> DRIZZLE_DENSE
                56 -> FREEZING_DRIZZLE_LIGHT
                57 -> FREEZING_DRIZZLE_DENSE
                61 -> RAIN_SLIGHT
                63 -> RAIN_MODERATE
                65 -> RAIN_HEAVY
                66 -> FREEZING_RAIN_LIGHT
                67 -> FREEZING_RAIN_HEAVY
                71 -> SNOW_FALL_SLIGHT
                73 -> SNOW_FALL_MODERATE
                75 -> SNOW_FALL_HEAVY
                77 -> SNOW_GRAINS
                80 -> RAIN_SHOWERS_SLIGHT
                81 -> RAIN_SHOWERS_MODERATE
                82 -> RAIN_SHOWERS_VIOLENT
                85 -> SNOW_SHOWERS_SLIGHT
                86 -> SNOW_SHOWERS_HEAVY
                95 -> THUNDERSTORM
                96 -> THUNDERSTORM_HAIL_SLIGHT
                99 -> THUNDERSTORM_HAIL_HEAVY
                else -> UNKNOWN
            }
        }
    }

    override fun toString(): String = description
}
