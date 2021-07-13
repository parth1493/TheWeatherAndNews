package com.parthdesai.theweatherandnews.api.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @Expose
    @SerializedName("list")
    val listWeatherResponse: List<WeatherForecastListResponse>? = null
)
