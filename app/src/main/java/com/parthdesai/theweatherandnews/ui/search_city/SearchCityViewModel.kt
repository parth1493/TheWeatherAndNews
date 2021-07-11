package com.parthdesai.theweatherandnews.ui.search_city

import androidx.lifecycle.LiveData
import com.parthdesai.theweatherandnews.models.SearchCurrentCityWeather
import com.parthdesai.theweatherandnews.repository.weather.SearchCurrentCityWeatherRepository
import com.parthdesai.theweatherandnews.ui.BaseViewModel
import com.parthdesai.theweatherandnews.ui.DataState
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchCurrentCityStateEvent
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchCurrentCityViewState
import com.parthdesai.theweatherandnews.util.AbsentLiveData
import com.parthdesai.theweatherandnews.util.GenericApiResponse
import javax.inject.Inject

class SearchCityViewModel
@Inject
constructor(
    val repository: SearchCurrentCityWeatherRepository
): BaseViewModel<SearchCurrentCityStateEvent, SearchCurrentCityViewState>()
{
    override fun handleStateEvent(stateEvent: SearchCurrentCityStateEvent): LiveData<DataState<SearchCurrentCityViewState>> =
        when(stateEvent){

            is SearchCurrentCityStateEvent.SearchCurrentCityEvent -> {
                repository.searchWeatherByCityName(
                    stateEvent.cityName
                )
            }

            is SearchCurrentCityStateEvent.CheckPreviousSearchedCityEvent -> {
                repository.checkPreviousSearchedCityEvent()
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