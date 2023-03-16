package com.example.transweather.features.search.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.transweather.R
import com.example.transweather.core.basefragment.BaseFragment
import com.example.transweather.core.state.State
import com.example.transweather.databinding.FragmentSearchBinding
import com.example.transweather.features.common.viewmodel.MainViewModel
import com.example.transweather.features.search.data.model.response.LocationResponseDto
import com.example.transweather.features.search.presentation.adapter.LocationsAdapter
import com.example.transweather.features.search.presentation.viewmodel.SearchViewModel
import com.google.android.gms.location.LocationServices
import com.pluto.plugin.utilities.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

const val LOCATION_CLICKED = "LOCATION_CLICKED"
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    val searchViewModel: SearchViewModel by viewModels()
    val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getDeviceLocation()
        } else {
            toast(getString(R.string.location_permission_error))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch { collectSearchState() }
        handleKeyboardSearch()
        handleRadioButtonsSelection()
        binding.locateMeBtn.setOnClickListener { onLocateButtonClicked() }
    }

    private suspend fun collectSearchState() {
        searchViewModel.searchState.collect {
            binding.progress.isVisible = false
            when (it) {
                is State.Initial -> {} // todo show search for a location animation
                is State.Error -> handleError(it)
                is State.Loading -> binding.progress.isVisible = true
                is State.Success -> showLocationResult(it.data)
            }
        }
    }

    private fun handleError(error: State.Error<*>) {
        if (error.exception is HttpException && error.exception.code() == 404) {
            showCanNotFindLocationToast()
        }
        Timber.w(error.message ?: "search error", error.exception)
    }

    private fun showLocationResult(locations: List<LocationResponseDto>?) {
        if (locations.isNullOrEmpty()) {
            showCanNotFindLocationToast()
            return
        }
        val adapter = LocationsAdapter{
            mainViewModel.setSearchLocation(it)
            setFragmentResult(LOCATION_CLICKED, bundleOf(LOCATION_CLICKED to true))
            findNavController().popBackStack()
        }
        adapter.locations = locations
        binding.citiesRv.adapter = adapter
    }

    private fun showCanNotFindLocationToast() = toast(getString(R.string.can_not_find_location))


    private fun handleKeyboardSearch() {
        listOf(binding.searchEt, binding.latEt, binding.lonEt).onEach {
            it.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getLocation()
                    return@setOnEditorActionListener true
                }
                false
            }

        }
    }

    private fun getLocation() {
        val query = binding.searchEt.text.toString()
        if (binding.cityNameRb.isChecked) {
            searchViewModel.searchLocationsByName(query)
        } else if (binding.zipCodeRb.isChecked) {
            searchViewModel.getLocationByZip(query)
        } else if (binding.latLongRb.isChecked) {
            handleGetLatLonLocation()
        }
    }

    private fun handleGetLatLonLocation() {
        val latEt = binding.latEt
        val lonEt = binding.lonEt
        when {
            latEt.text.isNullOrEmpty() -> latEt.requestFocus()
            lonEt.text.isNullOrEmpty() -> latEt.requestFocus()
            else -> searchViewModel.searchLocationByLatAndLong(
                latEt.text.toString(),
                lonEt.text.toString()
            )
        }
    }

    private fun handleRadioButtonsSelection() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkId ->
            handleSearchBarsVisibility(checkId != R.id.latLongRb)
        }
    }

    private fun handleSearchBarsVisibility(showSearchBar: Boolean) {
        binding.latLongGroup.isVisible = !showSearchBar
        binding.searchTil.isVisible = showSearchBar
    }

    private fun onLocateButtonClicked() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location == null) {
                toast(getString(R.string.failed_to_get_location))
                return@addOnSuccessListener
            }
            binding.latEt.setText(location.latitude.toString())
            binding.lonEt.setText(location.longitude.toString())
            handleGetLatLonLocation()
        }
    }

}