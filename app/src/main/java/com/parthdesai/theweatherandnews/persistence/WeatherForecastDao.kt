package com.parthdesai.theweatherandnews.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather
import com.parthdesai.theweatherandnews.models.WeatherForecast

@Dao
interface WeatherForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherForecast(weatherForecast: WeatherForecast)

    @Query("SELECT * FROM weather_forecast")
    fun getAllData(): List<WeatherForecast>

    @Query("Select count(*) from weather_forecast")
    suspend fun getCount(): Int

    @Query("DELETE FROM weather_forecast")
    fun nukeTable()
}