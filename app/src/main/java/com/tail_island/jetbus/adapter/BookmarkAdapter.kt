package com.tail_island.jetbus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tail_island.jetbus.databinding.ListItemBookmarkBinding
import com.tail_island.jetbus.model.Bookmark

class BookmarkAdapter: ListAdapter<Bookmark, BookmarkAdapter.ViewHolder>(DiffCallback()) {
    lateinit var onBookmarkClick: (bookmark: Bookmark) -> Unit

    inner class ViewHolder(private val binding: ListItemBookmarkBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bookmark) {
            binding.item = item
            binding.executePendingBindings()

            binding.bookmarkButton.setOnClickListener {
                onBookmarkClick(item)
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Bookmark>() {
        override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark) = oldItem == newItem
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ListItemBookmarkBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(getItem(position))
}
