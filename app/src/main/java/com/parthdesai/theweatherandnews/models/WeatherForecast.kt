package com.parthdesai.theweatherandnews.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_forecast")
data class WeatherForecast(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "icon")
    var icon: String,

    @ColumnInfo(name = "temp_min")
    var temp_min: String? = null,

    @ColumnInfo(name = "temp_max")
    var temp_max: String? = null
) {
    override fun toString(): String {
        return "WeatherForecast(id=$id, icon='$icon', temp_min=$temp_min, temp_max=$temp_max)"
    }
}
