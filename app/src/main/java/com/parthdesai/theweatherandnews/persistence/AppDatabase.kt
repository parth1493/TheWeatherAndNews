package com.parthdesai.theweatherandnews.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather

@Database(entities = [SearchByCurrentCityWeather::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSearchCurrentCityWeatherDao(): SearchCurrentCityWeatherDao

    companion object{
        val DATABASE_NAME: String = "app_db"
    }
}