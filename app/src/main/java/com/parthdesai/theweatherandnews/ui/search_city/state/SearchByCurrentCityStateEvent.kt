package com.parthdesai.theweatherandnews.ui.search_city.state

sealed class SearchByCurrentCityStateEvent {

    data class SearchByCurrentCityEvent(
        val cityName:String
    ): SearchByCurrentCityStateEvent()

    class CheckPreviousSearchedCityWeatherEventBy(): SearchByCurrentCityStateEvent()

    data class WeatherForecastEvent(
        val cityName:String
    ): SearchByCurrentCityStateEvent()

    class CheckPreviousWeatherForecastEventBy(): SearchByCurrentCityStateEvent()

}