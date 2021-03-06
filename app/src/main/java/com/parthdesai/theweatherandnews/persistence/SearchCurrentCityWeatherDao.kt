package com.parthdesai.theweatherandnews.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather

@Dao
interface SearchCurrentCityWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(searchByCurrentCityWeatherEntity: SearchByCurrentCityWeather)

    @Query("SELECT * FROM search_current_city_weather WHERE id = 1")
    fun searchById(): SearchByCurrentCityWeather

    @Query("Select count(*) from search_current_city_weather")
    suspend fun getCount(): Int

    @Query("UPDATE search_current_city_weather SET name = :name, icon1 = :icon, temperature = :temperature, temp_min =:temp_min, temp_max=:temp_max WHERE id = :id")
    fun updateCurrentCity(id: Int, name: String, icon: String, temperature: String, temp_min: String, temp_max: String )

}