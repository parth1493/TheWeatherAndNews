package com.parthdesai.theweatherandnews.di

import com.parthdesai.theweatherandnews.ui.search_city.SearchByCurrentCityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): SearchByCurrentCityFragment
}