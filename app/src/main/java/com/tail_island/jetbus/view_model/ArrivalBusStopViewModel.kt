package com.tail_island.jetbus.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tail_island.jetbus.getBusStopIndexes
import com.tail_island.jetbus.model.Repository

class ArrivalBusStopViewModel(private val repository: Repository): ViewModel() {
    val departureBusStopName = MutableLiveData<String>()

    val arrivalBusStops = Transformations.switchMap(departureBusStopName) {
        repository.getObservableBusStopsByDepartureBusStopName(it)
    }

    val arrivalBusStopIndexes = Transformations.map(arrivalBusStops) {
        getBusStopIndexes(it)
    }
}
