package com.parthdesai.theweatherandnews.di.weather

import androidx.lifecycle.ViewModel
import com.parthdesai.theweatherandnews.di.ViewModelKey
import com.parthdesai.theweatherandnews.ui.search_city.SearchCityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchCurrentCityWeatherViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchCityViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: SearchCityViewModel): ViewModel
}