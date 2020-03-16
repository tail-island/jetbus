package com.tail_island.jetbus.view_model

import androidx.lifecycle.ViewModel
import com.tail_island.jetbus.model.Repository

class MainViewModel(private val repository: Repository): ViewModel() {
    suspend fun clearDatabase() = repository.clearDatabase()
}
