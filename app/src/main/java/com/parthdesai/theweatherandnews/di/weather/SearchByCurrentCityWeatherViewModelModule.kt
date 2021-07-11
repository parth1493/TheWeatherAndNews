package com.parthdesai.theweatherandnews.di.weather

import androidx.lifecycle.ViewModel
import com.parthdesai.theweatherandnews.di.ViewModelKey
import com.parthdesai.theweatherandnews.ui.search_city.SearchByCurrentCityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchByCurrentCityWeatherViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchByCurrentCityViewModel::class)
    abstract fun bindAuthViewModel(authViewModelByCurrent: SearchByCurrentCityViewModel): ViewModel
}