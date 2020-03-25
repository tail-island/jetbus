package com.tail_island.jetbus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tail_island.jetbus.databinding.ListItemBusApproachBinding
import com.tail_island.jetbus.model.BusApproach

class BusApproachAdapter: ListAdapter<BusApproach, BusApproachAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(private val binding: ListItemBusApproachBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BusApproach) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<BusApproach>() {
        override fun areItemsTheSame(oldItem: BusApproach, newItem: BusApproach) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: BusApproach, newItem: BusApproach) = oldItem == newItem
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ListItemBusApproachBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(getItem(position))
}
