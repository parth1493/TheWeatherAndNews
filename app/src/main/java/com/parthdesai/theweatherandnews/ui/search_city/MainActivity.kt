package com.parthdesai.theweatherandnews.ui.search_city

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.parthdesai.theweatherandnews.R
import com.parthdesai.theweatherandnews.databinding.ActivityMainBinding
import com.parthdesai.theweatherandnews.ui.BaseActivity
import com.parthdesai.theweatherandnews.ui.search_city.state.SearchByCurrentCityStateEvent
import com.parthdesai.theweatherandnews.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class MainActivity : BaseActivity()
{
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: SearchCityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this, providerFactory).get(SearchCityViewModel::class.java)

        subscribeObservers()
        initFragment()
        binding.searchEditText.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                Log.d(TAG ,binding.searchEditText.text.toString())
                    searchByCity()
                true
            } else {
                false
            }
        }

        checkPreviousSearchedCityWeatherUser()
    }

    private fun searchByCity(){
        viewModel.setStateEvent(
            SearchByCurrentCityStateEvent.SearchByCurrentCityEvent(
                binding.searchEditText.text.toString()
            )
        )
    }

    private fun checkPreviousSearchedCityWeatherUser(){
        viewModel.setStateEvent(SearchByCurrentCityStateEvent.CheckPreviousSearchedCityWeatherEventBy())
    }

    private fun initFragment(){
        val textFragment = SearchCityFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_content_main,textFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun subscribeObservers(){
        viewModel.dataState.observe(this, Observer { dataState->
            onDataStateChange(dataState)
        })
    }

    override fun displayProgressBar(bool: Boolean) {
        if(bool){
            binding.contentView.progressBar.visibility = View.VISIBLE
        }
        else{
            binding.contentView.progressBar.visibility = View.GONE
        }
    }
}