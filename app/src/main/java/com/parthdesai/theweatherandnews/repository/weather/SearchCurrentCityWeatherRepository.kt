package com.parthdesai.theweatherandnews.repository.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.parthdesai.theweatherandnews.api.OpenApi
import com.parthdesai.theweatherandnews.api.weather.SearchCurrentCityWeatherResponse
import com.parthdesai.theweatherandnews.models.SearchCurrentCityWeather
import com.parthdesai.theweatherandnews.persistence.SearchCurrentCityWeatherDao
import com.parthdesai.theweatherandnews.repository.JobManager
import com.parthdesai.theweatherandnews.repository.NetworkBoundResource
import com.parthdesai.theweatherandnews.ui.DataState
import com.parthdesai.theweatherandnews.ui.Response
import com.parthdesai.theweatherandnews.ui.ResponseType
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchCurrentCityField
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchCurrentCityViewState
import com.parthdesai.theweatherandnews.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchCurrentCityWeatherRepository
@Inject
constructor(
    val openApi: OpenApi,
    val searchCurrentCityWeatherDao: SearchCurrentCityWeatherDao
): JobManager("SearchCurrentCityWeatherRepository")
{

    private val TAG: String = "AppDebug"

    fun searchWeatherByCityName(cityName: String): LiveData<DataState<SearchCurrentCityViewState>> {

        val loginFieldErrors = SearchCurrentCityField(cityName).isValid()

        if(loginFieldErrors != SearchCurrentCityField.SearchCurrentCityError.none()){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<SearchCurrentCityWeatherResponse, SearchCurrentCityWeather, SearchCurrentCityViewState>(
            Constants.isNetworkConnected,
            true,
            true,
            false
        ){

            // not used in this case
            override fun loadFromCache(): LiveData<SearchCurrentCityViewState> {
                return AbsentLiveData.create()
            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {
            }

            // not used in this case
            override suspend fun updateLocalDb(cacheObject: SearchCurrentCityWeather?) {
                searchCurrentCityWeatherDao.insertCurrentWeather(cacheObject!!)
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<SearchCurrentCityWeatherResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")

                val searchCurrentCityWeather = SearchCurrentCityWeather(
                    1,
                    response.body.name,
                    response.body.weather?.get(0)!!.icon,
                    response.body.main?.temp.toString(),
                    response.body.main?.tempMin.toString(),
                    response.body.main?.tempMax.toString())

                updateLocalDb(searchCurrentCityWeather)

                onCompleteJob(
                    DataState.data(
                        SearchCurrentCityViewState(
                            searchCurrentCityWeather = searchCurrentCityWeather
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<SearchCurrentCityWeatherResponse>> {
                return openApi.searchCity(cityName)
            }

            override fun setJob(job: Job) {
                addJob("getCurrentCityWeatherData", job)
            }
        }.asLiveData()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<SearchCurrentCityViewState>>{
        Log.d(TAG, "returnErrorResponse: $errorMessage")

        return object: LiveData<DataState<SearchCurrentCityViewState>>(){
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

    fun checkPreviousSearchedCityEvent(): LiveData<DataState<SearchCurrentCityViewState>> {
        return object: NetworkBoundResource<SearchCurrentCityWeatherResponse,Any, SearchCurrentCityViewState>(
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
                                    SearchCurrentCityViewState(searchCurrentCityWeather =  checkPreviousSearchedCityData)
                                )
                            )
                        }
                    }
                }
            }

            // not used in this case
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<SearchCurrentCityWeatherResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: $response")

            }

            override fun createCall(): LiveData<GenericApiResponse<SearchCurrentCityWeatherResponse>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("CheckPreviousCityWeatherData", job)
            }

            // Ignore
            override fun loadFromCache(): LiveData<SearchCurrentCityViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

        }.asLiveData()
    }
}
