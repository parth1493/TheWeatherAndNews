package com.parthdesai.theweatherandnews.di.weather

import com.parthdesai.theweatherandnews.api.OpenApi
import com.parthdesai.theweatherandnews.persistence.SearchCurrentCityWeatherDao
import com.parthdesai.theweatherandnews.persistence.WeatherForecastDao
import com.parthdesai.theweatherandnews.repository.searchCurrentCityWeather.SearchByCurrentCityWeatherRepository
import com.parthdesai.theweatherandnews.repository.weatherforecast.WeatherForecastRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class WeatherModule{


    @Provides
    fun provideOpenApiAuthService(retrofitBuilder: Retrofit.Builder): OpenApi {
        return retrofitBuilder
            .build()
            .create(OpenApi::class.java)
    }

    @Provides
    fun provideSearchCurrentCityWeatherRepository(
        openApi: OpenApi,
        searchCurrentCityWeatherDao: SearchCurrentCityWeatherDao
    ): SearchByCurrentCityWeatherRepository {
        return SearchByCurrentCityWeatherRepository(
            openApi,
            searchCurrentCityWeatherDao
        )
    }

    @Provides
    fun provideSearchCurrentCityWeatherForecastRepository(
        openApi: OpenApi,
        weatherForecastDao: WeatherForecastDao
    ): WeatherForecastRepository {
        return WeatherForecastRepository(
            openApi,
            weatherForecastDao
        )
    }

}