package com.parthdesai.theweatherandnews.ui.search_city.state

sealed class SearchCurrentCityStateEvent {

    data class SearchCurrentCityEvent(
        val cityName:String
    ): SearchCurrentCityStateEvent()

    class CheckPreviousSearchedCityEvent(): SearchCurrentCityStateEvent()
}