package com.tail_island.jetbus.view_model

import androidx.lifecycle.ViewModel
import com.tail_island.jetbus.model.Repository

class BookmarksViewModel(private val repository: Repository): ViewModel() {
    val bookmarks = repository.getObservableBookmarks()
}