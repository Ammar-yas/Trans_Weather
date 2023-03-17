package com.example.transweather.features.dashboard.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.transweather.R
import com.example.transweather.core.basefragment.BaseFragment
import com.example.transweather.core.state.State
import com.example.transweather.databinding.FragmentDashboardBinding
import com.example.transweather.features.common.viewmodel.MainViewModel
import com.example.transweather.features.current_weather.domain.model.CurrentWeatherUIModel
import com.pluto.plugin.utilities.extensions.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DashBoardFragment : BaseFragment<FragmentDashboardBinding>(R.layout.fragment_dashboard) {

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectCurrentWeatherState()
        binding.forecastBtn.setOnClickListener { navigate(R.id.action_dashBoardFragment_to_forecastFragment) }
        binding.currentWeatherBtn.setOnClickListener { navigate(R.id.action_dashBoardFragment_to_currentWeatherFragment) }
    }

    private fun collectCurrentWeatherState() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.currentWeatherState.collectLatest {
                binding.progress.isVisible = false
                when (it) {
                    is State.Initial -> mainViewModel.getCurrentWeather()
                    is State.Loading -> binding.progress.isVisible = true
                    is State.Error -> toast(getString(R.string.something_went_wriong))
                    is State.Success -> handleSuccessState(it.data)
                }
            }

        }
    }

    private fun handleSuccessState(weatherDto: CurrentWeatherUIModel?) {
        showSearchGroup(weatherDto == null)
        weatherDto ?: return
        binding.locationTv.text = String.format("%s, %s", weatherDto.name, weatherDto.temp)
        Glide.with(this).load(weatherDto.iconUrl).into(binding.iconIv)
    }

    private fun showSearchGroup(show: Boolean) {
        binding.weatherInfoGroup.isVisible = !show
        binding.startBySearchGroup.isVisible = show
    }
}