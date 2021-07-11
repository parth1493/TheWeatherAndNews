package com.parthdesai.theweatherandnews.repository.weather

import android.util.Log
import androidx.lifecycle.LiveData
import com.parthdesai.theweatherandnews.api.OpenApi
import com.parthdesai.theweatherandnews.api.weather.SearchByCurrentCityWeatherResponse
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather
import com.parthdesai.theweatherandnews.persistence.SearchCurrentCityWeatherDao
import com.parthdesai.theweatherandnews.repository.JobManager
import com.parthdesai.theweatherandnews.repository.NetworkBoundResource
import com.parthdesai.theweatherandnews.ui.DataState
import com.parthdesai.theweatherandnews.ui.Response
import com.parthdesai.theweatherandnews.ui.ResponseType
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityField
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityViewState
import com.parthdesai.theweatherandnews.util.*
import kotlinx.coroutines.Job
import javax.inject.Inject

class SearchByCurrentCityWeatherRepository
@Inject
constructor(
    val openApi: OpenApi,
    val searchCurrentCityWeatherDao: SearchCurrentCityWeatherDao
): JobManager("SearchCurrentCityWeatherRepository")
{

    private val TAG: String = "AppDebug"

    fun searchCurrentWeatherByCityName(cityName: String): LiveData<DataState<SearchByCurrentCityViewState>> {

        val loginFieldErrors = SearchByCurrentCityField(cityName).isValid()

        if(loginFieldErrors != SearchByCurrentCityField.SearchByCurrentCityError.none()){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<SearchByCurrentCityWeatherResponse, SearchByCurrentCityWeather, SearchByCurrentCityViewState>(
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
            override suspend fun updateLocalDb(cacheObject: SearchByCurrentCityWeather?) {
                searchCurrentCityWeatherDao.insertCurrentWeather(cacheObject!!)
            }

            override suspend fun handleApiSuccessResponse(responseBy: ApiSuccessResponse<SearchByCurrentCityWeatherResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $responseBy")

                val searchCurrentCityWeather = SearchByCurrentCityWeather(
                    1,
                    responseBy.body.name,
                    responseBy.body.weather?.get(0)!!.icon,
                    responseBy.body.main?.temp.toString(),
                    responseBy.body.main?.tempMin.toString(),
                    responseBy.body.main?.tempMax.toString())

                updateLocalDb(searchCurrentCityWeather)

                onCompleteJob(
                    DataState.data(
                        SearchByCurrentCityViewState(
                            searchByCurrentCityWeather = searchCurrentCityWeather
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<SearchByCurrentCityWeatherResponse>> {
                return openApi.searchCity(cityName)
            }

            override fun setJob(job: Job) {
                addJob("getCurrentCityWeatherData", job)
            }
        }.asLiveData()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<SearchByCurrentCityViewState>>{
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
        return object: NetworkBoundResource<SearchByCurrentCityWeatherResponse,Any, SearchByCurrentCityViewState>(
            Constants.isNetworkConnected,
            false,
            false,
            false
        ){

            override suspend fun createCacheRequestAndReturn() {
                if(searchCurrentCityWeatherDao.getCount() >- 1){
                    searchCurrentCityWeatherDao.searchById().let { checkPreviousSearchedCityData ->
                        checkPreviousSearchedCityData.let {
                            onCompleteJob(
                                DataState.data(
                                    SearchByCurrentCityViewState(searchByCurrentCityWeather =  checkPreviousSearchedCityData)
                                )
                            )
                        }
                    }
                }
            }

            // not used in this case
            override suspend fun handleApiSuccessResponse(responseBy: ApiSuccessResponse<SearchByCurrentCityWeatherResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $responseBy")

            }

            override fun createCall(): LiveData<GenericApiResponse<SearchByCurrentCityWeatherResponse>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("CheckPreviousCityWeatherData", job)
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
