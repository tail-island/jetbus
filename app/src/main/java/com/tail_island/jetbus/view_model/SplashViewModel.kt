package com.tail_island.jetbus.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tail_island.jetbus.model.Repository
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository): ViewModel() {
    val isSyncDatabaseFinished = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            repository.syncDatabase() ?: run { isSyncDatabaseFinished.value = false; return@launch }

            isSyncDatabaseFinished.value = true
        }
    }
}
