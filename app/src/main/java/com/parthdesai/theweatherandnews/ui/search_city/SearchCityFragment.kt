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
import com.parthdesai.theweatherandnews.models.SearchCurrentCityWeather
import com.parthdesai.theweatherandnews.ui.BaseFragment
import com.parthdesai.theweatherandnews.util.Constants.Companion.IMAGE_URL

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SearchCityFragment : BaseFragment() {

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
        Log.d(TAG, "SearchCityFragment Parth: ${viewModel}")

        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.searchCurrentCityWeather?.let {
                            Log.d(TAG, "SearchCurrentCityWeather: ${it}")
                            viewModel.setSearchByCurrentCity(it)
                            setAccountDataFields(it)
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setAccountDataFields(searchCurrentCityWeather: SearchCurrentCityWeather){
        Log.d("Parth--4", searchCurrentCityWeather.name+" , "+ searchCurrentCityWeather.icon)
        binding.todayIn.visibility = View.VISIBLE
        binding.cityName.visibility = View.VISIBLE
        binding.minMaxTemp.visibility = View.VISIBLE
        binding.current.visibility = View.VISIBLE
        binding.icon.visibility = View.VISIBLE

        binding.cityName.text = searchCurrentCityWeather.name
        binding.minMaxTemp.text = "max ${searchCurrentCityWeather.temp_max} / min ${searchCurrentCityWeather.temp_min}"
        binding.current.text = "Current ${searchCurrentCityWeather.temperature}"
        Glide.with(binding.icon.context)
            .asBitmap()
            .load("${IMAGE_URL}${searchCurrentCityWeather.icon}.png")
            .into(binding.icon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}