package com.parthdesai.theweatherandnews.ui

interface DataStateChangeListener{

    fun onDataStateChange(dataState: DataState<*>?)
}