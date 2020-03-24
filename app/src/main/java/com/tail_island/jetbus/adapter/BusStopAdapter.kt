package com.tail_island.jetbus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tail_island.jetbus.databinding.ListItemBusStopBinding
import com.tail_island.jetbus.model.BusStop

class BusStopAdapter: ListAdapter<BusStop, BusStopAdapter.ViewHolder>(DiffCallback()) {
    lateinit var onBusStopClick: (busStop: BusStop) -> Unit

    inner class ViewHolder(private val binding: ListItemBusStopBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusStop) {
            binding.item = item
            binding.executePendingBindings()

            binding.busStopButton.setOnClickListener {
                onBusStopClick(item)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<BusStop>() {
        override fun areItemsTheSame(oldItem: BusStop, newItem: BusStop) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: BusStop, newItem: BusStop) = oldItem == newItem
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ListItemBusStopBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(getItem(position))
}
