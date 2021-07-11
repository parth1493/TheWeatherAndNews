package com.parthdesai.theweatherandnews.ui.search_city

import androidx.lifecycle.LiveData
import com.parthdesai.theweatherandnews.models.SearchCurrentCityWeather
import com.parthdesai.theweatherandnews.repository.weather.SearchCurrentCityWeatherRepository
import com.parthdesai.theweatherandnews.ui.BaseViewModel
import com.parthdesai.theweatherandnews.ui.DataState
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityStateEvent
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchCurrentCityViewState
import javax.inject.Inject

class SearchCityViewModel
@Inject
constructor(
    val repository: SearchCurrentCityWeatherRepository
): BaseViewModel<SearchByCurrentCityStateEvent, SearchCurrentCityViewState>()
{
    override fun handleStateEvent(stateEventBy: SearchByCurrentCityStateEvent): LiveData<DataState<SearchCurrentCityViewState>> =
        when(stateEventBy){

            is SearchByCurrentCityStateEvent.SearchByCurrentCityEvent -> {
                repository.searchCurrentWeatherByCityName(
                    stateEventBy.cityName
                )
            }

            is SearchByCurrentCityStateEvent.CheckPreviousSearchedCityWeatherEventBy -> {
                repository.checkPreviousSearchedCityWeatherEvent()
            }
        }

    override fun initNewViewState(): SearchCurrentCityViewState {
        return SearchCurrentCityViewState()
    }

    fun setSearchByCurrentCity(searchCurrentCityWeather: SearchCurrentCityWeather){
        val update = getCurrentViewStateOrNew()
        if(update.searchCurrentCityWeather == searchCurrentCityWeather){
            return
        }
        update.searchCurrentCityWeather = searchCurrentCityWeather
        _viewState.value = update
    }

    fun cancelActiveJobs(){
        repository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}