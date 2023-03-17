package com.example.transweather.features.forecast.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.transweather.R
import com.example.transweather.core.basefragment.BaseFragment
import com.example.transweather.core.state.State
import com.example.transweather.databinding.FragmentForecastBinding
import com.example.transweather.features.common.adapters.SearchHistoryAdapter
import com.example.transweather.features.common.viewmodel.MainViewModel
import com.example.transweather.features.forecast.domain.model.ForecastUiModel
import com.example.transweather.features.forecast.domain.model.TempIntervalUiModel
import com.example.transweather.features.forecast.presentation.adapter.ForecastAdapter
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.presentation.LOCATION_CLICKED
import com.pluto.plugin.utilities.extensions.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class ForecastFragment : BaseFragment<FragmentForecastBinding>(R.layout.fragment_forecast) {

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchHistoryRv()
        setupForecastRv()
        setupRadioGroup()
        collectCurrentWeatherState()
        binding.searchBtn.setOnClickListener { navigate(R.id.action_currentWeatherFragment_to_searchFragment) }
        setFragmentResultListener(LOCATION_CLICKED) { _, bundle ->
            if (bundle.getBoolean(LOCATION_CLICKED)) {
                mainViewModel.getCurrentForecast()
            }
        }
    }

    private fun setupSearchHistoryRv() {
        binding.searchHistoryRv.adapter = SearchHistoryAdapter {
            mainViewModel.getForecastForLocation(it, isOnlyOneDay())
        }
        PagerSnapHelper().attachToRecyclerView(binding.searchHistoryRv)
    }

    private fun setupForecastRv() {
        binding.forecastRv.adapter = ForecastAdapter()
    }

    private fun isOnlyOneDay(): Boolean? {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.twentyFourHours -> true
            R.id.fortyEightHours -> false
            else -> null
        }
    }

    private fun setupRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { _, _ ->

            val lastState = mainViewModel.forecastState.value
            if (lastState is State.Success) {
                val location = lastState.data?.location
                showSearchGroup(location == null)
                location ?: return@setOnCheckedChangeListener
                mainViewModel.getForecastForLocation(location, isOnlyOneDay())
            }
        }
    }

    private fun collectCurrentWeatherState() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.forecastState.collectLatest {
                binding.progress.isVisible = false
                when (it) {
                    is State.Initial -> mainViewModel.getCurrentForecast()
                    is State.Loading -> binding.progress.isVisible = true
                    is State.Error -> {
                        Timber.w(it.exception, it.message )
                        toast(getString(R.string.something_went_wriong))
                    }
                    is State.Success -> handleSuccessState(it.data)
                }
            }
        }
    }

    private fun handleSuccessState(weatherDto: ForecastUiModel?) {
        showSearchGroup(weatherDto == null)
        weatherDto ?: return
        binding.locationTv.text = weatherDto.name
        updateForecastRV(weatherDto.tempIntervals)
        updateSearchHistoryRv()
    }

    private fun showSearchGroup(show: Boolean) {
        binding.weatherInfoGroup.isVisible = !show
        binding.startBySearchGroup.isVisible = show
    }

    private fun updateForecastRV(tempIntervals: List<TempIntervalUiModel>) {
        val adapter = binding.forecastRv.adapter as? ForecastAdapter
        adapter?.intervals = tempIntervals
    }


    private fun updateSearchHistoryRv() {
        val currentList: List<LocationResponseDto> = mainViewModel.getSearchHistory(10)
        val adapter = binding.searchHistoryRv.adapter as? SearchHistoryAdapter
        adapter?.locations = currentList
    }


}

