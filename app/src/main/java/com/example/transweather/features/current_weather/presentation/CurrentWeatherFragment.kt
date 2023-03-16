package com.example.transweather.features.current_weather.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.example.transweather.R
import com.example.transweather.core.basefragment.BaseFragment
import com.example.transweather.core.state.State
import com.example.transweather.databinding.FragmentCurrentWeatherBinding
import com.example.transweather.features.common.adapters.SearchHistoryAdapter
import com.example.transweather.features.common.viewmodel.MainViewModel
import com.example.transweather.features.current_weather.domain.model.CurrentWeatherUIModel
import com.example.transweather.features.current_weather.domain.use_case.CurrentWeatherUseCase
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.presentation.LOCATION_CLICKED
import com.google.api.Distribution.BucketOptions.Linear
import com.pluto.plugin.utilities.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrentWeatherFragment :
    BaseFragment<FragmentCurrentWeatherBinding>(R.layout.fragment_current_weather) {

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchHistoryRv()
        collectCurrentWeatherState()
        binding.searchBtn.setOnClickListener { navigate(R.id.action_currentWeatherFragment_to_searchFragment) }
        setFragmentResultListener(LOCATION_CLICKED) { _,bundle->
            if (bundle.getBoolean(LOCATION_CLICKED)){
                mainViewModel.getCurrentWeather()
            }
        }
    }

    private fun setupSearchHistoryRv(){
        binding.searchHistoryRv.adapter =  SearchHistoryAdapter {
            mainViewModel.getWeatherForLocation(it)
        }
        PagerSnapHelper().attachToRecyclerView(binding.searchHistoryRv)
    }

    private fun collectCurrentWeatherState() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.currentWeatherState.collectLatest {
                binding.progress.isVisible = false
                when (it) {
                    is State.Initial -> mainViewModel.getCurrentWeather()
                    is State.Loading ->  binding.progress.isVisible = true
                    is State.Error -> toast(getString(R.string.something_went_wriong))
                    is State.Success -> handleSuccessState(it.data)
                }
            }

        }
    }



    private fun handleSuccessState(weatherDto: CurrentWeatherUIModel?) {
        showSearchGroup(weatherDto == null)
        weatherDto ?: return
        binding.locationTv.text = weatherDto.name
        Glide.with(this).load(weatherDto.iconUrl).into(binding.iconIv)
        binding.tempTv.text = weatherDto.temp
        binding.mainTv.text = weatherDto.main
        binding.descriptionTv.text = weatherDto.description
        var switchButtonTextRes = 0
        var switchUnit = CurrentWeatherUseCase.Unit.Celsius
        if (weatherDto.unit == CurrentWeatherUseCase.Unit.Celsius) {
            switchButtonTextRes = R.string.switch_to_fahrenheit
            switchUnit = CurrentWeatherUseCase.Unit.Fahrenheit
        } else {
            switchButtonTextRes = R.string.switch_to_celsius
            switchUnit = CurrentWeatherUseCase.Unit.Celsius
        }
        binding.switchBtn.text = getString(switchButtonTextRes)
        binding.switchBtn.setOnClickListener {
            mainViewModel.getWeatherForLocation(weatherDto.locationResponseDto, switchUnit)
        }
        updateSearchHistoryRv()
    }

    private fun showSearchGroup(show: Boolean) {
        binding.weatherInfoGroup.isVisible = !show
        binding.startBySearchGroup.isVisible = show
    }

    private fun updateSearchHistoryRv() {
        val currentList: List<LocationResponseDto> = mainViewModel.getSearchHistory(10)
        val adapter = binding.searchHistoryRv.adapter as? SearchHistoryAdapter
        adapter?.locations = currentList
    }

}