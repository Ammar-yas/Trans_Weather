package com.example.transweather.features.search.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.transweather.databinding.SearchItemBinding
import com.example.transweather.features.search.data.model.response.LocationResponseDto

class LocationsAdapter(private val onItemClicked: (LocationResponseDto) -> Unit) :
    RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {

    var locations: List<LocationResponseDto> = emptyList()
        @SuppressLint("NotifyDataSetChanged") // locations List will change each time
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(inflater, parent, false)
        return LocationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount() = locations.size

    inner class LocationsViewHolder(
        private val binding: SearchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationResponseDto) {
            binding.cityTv.text = location.name
            binding.detailsTv.text = String.format("%s", location.country)
            binding.root.setOnClickListener { onItemClicked(location) }
        }

    }
}