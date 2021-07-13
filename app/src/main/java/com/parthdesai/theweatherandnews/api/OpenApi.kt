package com.parthdesai.theweatherandnews.api

import androidx.lifecycle.LiveData
import com.parthdesai.theweatherandnews.BuildConfig
import com.parthdesai.theweatherandnews.api.weather.SearchByCurrentCityWeatherResponse
import com.parthdesai.theweatherandnews.api.weather.WeatherForecastResponse
import com.parthdesai.theweatherandnews.util.Constants
import com.parthdesai.theweatherandnews.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenApi {

    @GET("weather")
    fun searchCity(
        @Query("q") cityName:String,
        @Query("appid") apiKey:String = BuildConfig.WEATHER_KRY,
        @Query("units")units:String = Constants.UNITS
    ): LiveData<GenericApiResponse<SearchByCurrentCityWeatherResponse>>

    @GET("forecast")
    fun weatherForecast(
        @Query("q") cityName:String,
        @Query("appid") apiKey:String = BuildConfig.WEATHER_KRY,
        @Query("units")units:String = Constants.UNITS
    ): LiveData<GenericApiResponse<WeatherForecastResponse>>
}