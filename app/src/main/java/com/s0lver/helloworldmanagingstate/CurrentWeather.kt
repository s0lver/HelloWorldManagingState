package com.s0lver.helloworldmanagingstate

data class CurrentWeather(
    val weather: Weather,
    val temperature: Double,
    val minTemperature: Double,
    val maxTemperature: Double
)
