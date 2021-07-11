package com.parthdesai.theweatherandnews.ui.search_city.state

import com.parthdesai.theweatherandnews.models.SearchCurrentCityWeather

data class SearchCurrentCityViewState(
    var searchCurrentCityField: SearchCurrentCityField = SearchCurrentCityField(),
    var searchCurrentCityWeather: SearchCurrentCityWeather? = null
)

data class SearchCurrentCityField(
    var cityName:String? = null
){
    class SearchCurrentCityError{
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

            return SearchCurrentCityError.mustFillAllFields()
        }
        return SearchCurrentCityError.none()
    }

    override fun toString(): String {
        return "SearchCurrentCity(City Name=$cityName)"
    }
}