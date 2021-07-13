package com.parthdesai.theweatherandnews.ui.search_city

import androidx.lifecycle.LiveData
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather
import com.parthdesai.theweatherandnews.models.WeatherForecast
import com.parthdesai.theweatherandnews.repository.searchCurrentCityWeather.SearchByCurrentCityWeatherRepository
import com.parthdesai.theweatherandnews.repository.weatherforecast.WeatherForecastRepository
import com.parthdesai.theweatherandnews.ui.BaseViewModel
import com.parthdesai.theweatherandnews.ui.DataState
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityStateEvent
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityViewState
import javax.inject.Inject

class SearchByCurrentCityViewModel
@Inject
constructor(
    val searchByCurrentCity: SearchByCurrentCityWeatherRepository,
    val weatherForecastRepository: WeatherForecastRepository
): BaseViewModel<SearchByCurrentCityStateEvent, SearchByCurrentCityViewState>()
{
    override fun handleStateEvent(stateEventBy: SearchByCurrentCityStateEvent): LiveData<DataState<SearchByCurrentCityViewState>> =
        when(stateEventBy){

            is SearchByCurrentCityStateEvent.SearchByCurrentCityEvent -> {
                searchByCurrentCity.searchCurrentWeatherByCityName(
                    stateEventBy.cityName
                )
            }

            is SearchByCurrentCityStateEvent.CheckPreviousSearchedCityWeatherEventBy -> {
                searchByCurrentCity.checkPreviousSearchedCityWeatherEvent()
            }

            is SearchByCurrentCityStateEvent.WeatherForecastEvent ->
                weatherForecastRepository.getWeatherForecast(
                    stateEventBy.cityName
                )

            is SearchByCurrentCityStateEvent.CheckPreviousWeatherForecastEventBy ->
                weatherForecastRepository.checkPreviousSearchedCityWeatherEvent()
        }

    override fun initNewViewState(): SearchByCurrentCityViewState {
        return SearchByCurrentCityViewState()
    }

    fun setSearchByCurrentCity(searchByCurrentCityWeather: SearchByCurrentCityWeather){
        val update = getCurrentViewStateOrNew()
        if(update.searchByCurrentCityWeather == searchByCurrentCityWeather){
            return
        }
        update.searchByCurrentCityWeather = searchByCurrentCityWeather
        _viewState.value = update
    }

    fun setWeatherForecast(weatherForecast: List<WeatherForecast>){
        val update = getCurrentViewStateOrNew()
        if(update.weatherForecast == weatherForecast){
            return
        }
        update.weatherForecast = weatherForecast
        _viewState.value = update
    }

    fun cancelActiveJobs(){
        searchByCurrentCity.cancelActiveJobs()
        weatherForecastRepository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}