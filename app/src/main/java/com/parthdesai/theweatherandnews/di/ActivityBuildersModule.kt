package com.parthdesai.theweatherandnews.di

import com.parthdesai.theweatherandnews.di.weather.SearchCurrentCityWeatherViewModelModule
import com.parthdesai.theweatherandnews.di.weather.WeatherModule
import com.parthdesai.theweatherandnews.ui.search_city.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [WeatherModule::class, FragmentBuildersModule::class, SearchCurrentCityWeatherViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

}