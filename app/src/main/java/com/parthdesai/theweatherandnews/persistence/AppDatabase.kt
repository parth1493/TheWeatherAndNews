package com.parthdesai.theweatherandnews.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather
import com.parthdesai.theweatherandnews.models.WeatherForecast

@Database(entities = [SearchByCurrentCityWeather::class, WeatherForecast::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSearchCurrentCityWeatherDao(): SearchCurrentCityWeatherDao

    abstract fun getWeatherForecastDao(): WeatherForecastDao

    companion object{
        val DATABASE_NAME: String = "app_db"
    }
}