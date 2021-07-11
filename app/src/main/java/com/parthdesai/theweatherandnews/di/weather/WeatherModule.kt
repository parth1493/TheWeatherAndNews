package com.parthdesai.theweatherandnews.di.weather

import com.parthdesai.theweatherandnews.api.OpenApi
import com.parthdesai.theweatherandnews.api.weather.SearchCurrentCityWeatherResponse
import com.parthdesai.theweatherandnews.persistence.SearchCurrentCityWeatherDao
import com.parthdesai.theweatherandnews.repository.weather.SearchCurrentCityWeatherRepository
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
    ): SearchCurrentCityWeatherRepository {
        return SearchCurrentCityWeatherRepository(
            openApi,
            searchCurrentCityWeatherDao
        )
    }

}