package com.parthdesai.theweatherandnews.repository.weatherforecast

import android.util.Log
import androidx.lifecycle.LiveData
import com.parthdesai.theweatherandnews.api.OpenApi
import com.parthdesai.theweatherandnews.api.weather.SearchByCurrentCityWeatherResponse
import com.parthdesai.theweatherandnews.api.weather.WeatherForecastResponse
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather
import com.parthdesai.theweatherandnews.models.WeatherForecast
import com.parthdesai.theweatherandnews.persistence.WeatherForecastDao
import com.parthdesai.theweatherandnews.repository.JobManager
import com.parthdesai.theweatherandnews.repository.NetworkBoundResource
import com.parthdesai.theweatherandnews.ui.DataState
import com.parthdesai.theweatherandnews.ui.Response
import com.parthdesai.theweatherandnews.ui.ResponseType
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityField
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityViewState
import com.parthdesai.theweatherandnews.util.AbsentLiveData
import com.parthdesai.theweatherandnews.util.ApiSuccessResponse
import com.parthdesai.theweatherandnews.util.Constants
import com.parthdesai.theweatherandnews.util.GenericApiResponse
import kotlinx.coroutines.Job
import javax.inject.Inject

class WeatherForecastRepository
@Inject
constructor(
    val openApi: OpenApi,
    val weatherForecastDao: WeatherForecastDao
): JobManager("WeatherForecast")
{

    private val TAG: String = "AppDebug"

    fun getWeatherForecast(cityName: String): LiveData<DataState<SearchByCurrentCityViewState>> {

        val loginFieldErrors = SearchByCurrentCityField(cityName).isValid()

        if(loginFieldErrors != SearchByCurrentCityField.SearchByCurrentCityError.none()){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<WeatherForecastResponse, WeatherForecast, SearchByCurrentCityViewState>(
            Constants.isNetworkConnected,
            true,
            true,
            false
        ){

            // not used in this case
            override fun loadFromCache(): LiveData<SearchByCurrentCityViewState> {
                return AbsentLiveData.create()
            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {
            }

            // not used in this case
            override suspend fun updateLocalDb(cacheObject: WeatherForecast?) {
                weatherForecastDao.insertWeatherForecast(cacheObject!!)
            }

            override suspend fun handleApiSuccessResponse(responseBy: ApiSuccessResponse<WeatherForecastResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $responseBy")

                var forecast = responseBy.body.listWeatherResponse

                var forecastList = mutableListOf<WeatherForecast>()

                if(forecast != null) {
                    if (weatherForecastDao.getCount() > 0)  weatherForecastDao.nukeTable()

                    for(forecast in forecast) {
                       var weatherForecast = WeatherForecast(
                                icon = forecast.weather?.get(0)!!.icon,
                                temp_min = forecast.main?.tempMin.toString(),
                                temp_max = forecast.main?.tempMax.toString()
                       )
                        updateLocalDb(weatherForecast)
                    }
                    onCompleteJob(
                        DataState.data(
                            SearchByCurrentCityViewState(
                                weatherForecast = forecastList
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<WeatherForecastResponse>> {
                return openApi.weatherForecast(cityName)
            }

            override fun setJob(job: Job) {
                addJob("getCurrentCityWeatherData", job)
            }
        }.asLiveData()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<SearchByCurrentCityViewState>> {
        Log.d(TAG, "returnErrorResponse: $errorMessage")

        return object: LiveData<DataState<SearchByCurrentCityViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    fun checkPreviousSearchedCityWeatherEvent(): LiveData<DataState<SearchByCurrentCityViewState>> {
        return object: NetworkBoundResource<WeatherForecastResponse, Any, SearchByCurrentCityViewState>(
            Constants.isNetworkConnected,
            false,
            false,
            false
        ){

            override suspend fun createCacheRequestAndReturn() {
                if(weatherForecastDao.getCount() >- 1){
                    weatherForecastDao.getAllData().let { checkPreviousWeatherForecastData ->
                        checkPreviousWeatherForecastData.let {
                            onCompleteJob(
                                DataState.data(
                                    SearchByCurrentCityViewState(weatherForecast =  checkPreviousWeatherForecastData)
                                )
                            )
                        }
                    }
                }
            }

            // not used in this case
            override suspend fun handleApiSuccessResponse(responseBy: ApiSuccessResponse<WeatherForecastResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $responseBy")

            }

            override fun createCall(): LiveData<GenericApiResponse<WeatherForecastResponse>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("CheckPreviousWeatherData", job)
            }

            // Ignore
            override fun loadFromCache(): LiveData<SearchByCurrentCityViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

        }.asLiveData()
    }
}