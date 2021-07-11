package com.parthdesai.theweatherandnews.di

import androidx.lifecycle.ViewModelProvider
import com.parthdesai.theweatherandnews.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}