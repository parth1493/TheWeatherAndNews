package com.parthdesai.theweatherandnews.ui.search_city

import androidx.lifecycle.LiveData
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather
import com.parthdesai.theweatherandnews.repository.weather.SearchByCurrentCityWeatherRepository
import com.parthdesai.theweatherandnews.ui.BaseViewModel
import com.parthdesai.theweatherandnews.ui.DataState
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityStateEvent
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityViewState
import javax.inject.Inject

class SearchByCurrentCityViewModel
@Inject
constructor(
    val repositoryBy: SearchByCurrentCityWeatherRepository
): BaseViewModel<SearchByCurrentCityStateEvent, SearchByCurrentCityViewState>()
{
    override fun handleStateEvent(stateEventBy: SearchByCurrentCityStateEvent): LiveData<DataState<SearchByCurrentCityViewState>> =
        when(stateEventBy){

            is SearchByCurrentCityStateEvent.SearchByCurrentCityEvent -> {
                repositoryBy.searchCurrentWeatherByCityName(
                    stateEventBy.cityName
                )
            }

            is SearchByCurrentCityStateEvent.CheckPreviousSearchedCityWeatherEventBy -> {
                repositoryBy.checkPreviousSearchedCityWeatherEvent()
            }
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

    fun cancelActiveJobs(){
        repositoryBy.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}