package com.parthdesai.theweatherandnews.api.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherForecastListResponse(

    @Expose
    @SerializedName("weather")
    val weather: List<WeatherItemResponse?>? = null,

    @Expose
    @SerializedName("main")
    val main: MainResponse? = null
)
