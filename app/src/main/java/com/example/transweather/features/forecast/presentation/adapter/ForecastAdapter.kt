package com.example.transweather.features.forecast.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.transweather.databinding.ItemForecastBinding
import com.example.transweather.features.forecast.domain.model.TempIntervalUiModel

class ForecastAdapter() :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    var intervals: List<TempIntervalUiModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemForecastBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(intervals[position])
    }

    override fun getItemCount() = intervals.size


    inner class ViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(intervalUiModel: TempIntervalUiModel) {
            binding.dateTimeTv.text = intervalUiModel.dayTime
            binding.tempTv.text = intervalUiModel.temperature
            Glide.with(binding.iconIv).load(intervalUiModel.iconUrl).into(binding.iconIv)
        }
    }
}