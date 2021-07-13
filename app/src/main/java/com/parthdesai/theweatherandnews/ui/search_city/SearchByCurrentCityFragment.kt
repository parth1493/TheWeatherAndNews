package com.parthdesai.theweatherandnews.ui.search_city

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.parthdesai.theweatherandnews.databinding.FragmentFirstBinding
import com.parthdesai.theweatherandnews.models.SearchByCurrentCityWeather
import com.parthdesai.theweatherandnews.models.WeatherForecast
import com.parthdesai.theweatherandnews.ui.BaseFragment
import com.parthdesai.theweatherandnews.util.Constants.Companion.IMAGE_URL

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SearchByCurrentCityFragment : BaseFragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "SearchCityFragment Parth: ${viewModelByCurrent}")

        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModelByCurrent.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.searchByCurrentCityWeather?.let {
                            Log.d(TAG, "SearchCurrentCityWeather: ${it}")
                            viewModelByCurrent.setSearchByCurrentCity(it)
                            setCurrentCityFields(it)
                        }
                    }
                }
            }
        })

        viewModelByCurrent.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.weatherForecast?.let {
                            Log.d(TAG, "SearchCurrentCityWeather: ${it}")
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherDataDataFields(weatherForecast: List<WeatherForecast>){
        for (forecast in weatherForecast) {
            Log.d("Parth--5", forecast.temp_max + " , " + forecast.temp_min)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentCityFields(searchByCurrentCityWeather: SearchByCurrentCityWeather){
        Log.d("Parth--4", searchByCurrentCityWeather.name+" , "+ searchByCurrentCityWeather.icon)
        binding.todayIn.visibility = View.VISIBLE
        binding.cityName.visibility = View.VISIBLE
        binding.minMaxTemp.visibility = View.VISIBLE
        binding.current.visibility = View.VISIBLE
        binding.icon.visibility = View.VISIBLE

        binding.cityName.text = searchByCurrentCityWeather.name
        binding.minMaxTemp.text = "max ${searchByCurrentCityWeather.temp_max} / min ${searchByCurrentCityWeather.temp_min}"
        binding.current.text = "Current ${searchByCurrentCityWeather.temperature}"
        Glide.with(binding.icon.context)
            .asBitmap()
            .load("${IMAGE_URL}${searchByCurrentCityWeather.icon}.png")
            .into(binding.icon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}