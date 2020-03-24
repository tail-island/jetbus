package com.tail_island.jetbus.view_model

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tail_island.jetbus.getBusStopIndexes
import com.tail_island.jetbus.model.Repository

class DepartureBusStopViewModel(private val repository: Repository): ViewModel() {
    val departureBusStops = repository.getObservableBusStops()

    val departureBusStopIndexes = Transformations.map(departureBusStops) {
        getBusStopIndexes(it)
    }
}