package com.example.transweather.features.common.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.transweather.databinding.ItemRecentSearchBinding
import com.example.transweather.features.search.data.model.response.LocationResponseDto

class SearchHistoryAdapter(private val onItemClicked: (LocationResponseDto) -> Unit) :
    RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    var locations: List<LocationResponseDto> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(locations[position])
    }

    override fun getItemCount() = locations.size


    inner class ViewHolder(private val binding: ItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationResponseDto) {
            binding.name.text = location.name
            Glide.with(binding.image).load(location.imageUrl).into(binding.image)
            binding.root.setOnClickListener { onItemClicked(location) }
        }
    }
}