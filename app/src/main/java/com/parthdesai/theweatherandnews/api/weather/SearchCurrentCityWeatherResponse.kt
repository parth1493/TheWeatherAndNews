package com.parthdesai.theweatherandnews.api.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchCurrentCityWeatherResponse (

    @Expose
    @SerializedName("cod")
    var code: String? = null,

    @Expose
    @SerializedName("message")
    var message: String? = null,

    @Expose
    @SerializedName("weather")
    val weather: List<WeatherItemResponse?>? = null,

    @Expose
    @SerializedName("name")
    val name: String? = null,

    @Expose
    @SerializedName("main")
    val main: MainResponse? = null,

    @Expose
    @SerializedName("id")
    val id: Long? = null
    )
    {
        override fun toString(): String {
            return "SearchCityResponse(code=$code, error_message=$message, weather=$weather, name=$name, main=$main, id=$id)"
        }

}