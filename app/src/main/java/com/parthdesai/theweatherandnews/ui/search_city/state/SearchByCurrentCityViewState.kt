package com.parthdesai.theweatherandnews.ui.search_city.state

import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather

data class SearchByCurrentCityViewState(
    var searchCurrentCityField: SearchByCurrentCityField = SearchByCurrentCityField(),
    var searchByCurrentCityWeather: SearchByCurrentCityWeather? = null
)

data class SearchByCurrentCityField(
    var cityName:String? = null
){
    class SearchByCurrentCityError{
        companion object{

            fun mustFillAllFields(): String{
                return "Please Enter city name"
            }

            fun none():String{
                return "None"
            }

        }
    }

    fun isValid(): String{

        if(cityName.isNullOrEmpty()
            || cityName.isNullOrEmpty()){

            return SearchByCurrentCityError.mustFillAllFields()
        }
        return SearchByCurrentCityError.none()
    }

    override fun toString(): String {
        return "SearchCurrentCity(City Name=$cityName)"
    }
}
