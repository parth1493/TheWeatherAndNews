package com.parthdesai.theweatherandnews.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.parthdesai.theweatherandnews.ui.search_city.SearchByCurrentCityViewModel
import com.parthdesai.theweatherandnews.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment: DaggerFragment(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModelByCurrent: SearchByCurrentCityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelByCurrent = activity?.run {
            ViewModelProvider(this, providerFactory).get(SearchByCurrentCityViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        cancelActiveJobs()
    }

    private fun cancelActiveJobs(){
        viewModelByCurrent.cancelActiveJobs()
    }
}