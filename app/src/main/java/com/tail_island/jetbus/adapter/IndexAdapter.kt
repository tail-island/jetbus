package com.tail_island.jetbus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tail_island.jetbus.databinding.ListItemIndexBinding

class IndexAdapter: ListAdapter<Char, IndexAdapter.ViewHolder>(DiffCallback()) {
    lateinit var onIndexClick: (index: Char) -> Unit

    inner class ViewHolder(private val binding: ListItemIndexBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Char) {
            binding.item = item
            binding.executePendingBindings()

            binding.indexButton.setOnClickListener {
                onIndexClick(item)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Char>() {
        override fun areItemsTheSame(oldItem: Char, newItem: Char) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Char, newItem: Char) = oldItem == newItem
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ListItemIndexBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    override fun onBindViewHolder(viewHolder: IndexAdapter.ViewHolder, position: Int) = viewHolder.bind(getItem(position))
 }
