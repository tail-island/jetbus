package com.tail_island.jetbus.view_model

import androidx.lifecycle.*
import com.tail_island.jetbus.model.Repository
import com.tail_island.jetbus.model.Route

class BusApproachesViewModel(private val repository: Repository): ViewModel() {
    val departureBusStopName = MutableLiveData<String>()

    val arrivalBusStops = Transformations.switchMap(departureBusStopName) { arrivalBusStopNameValue ->
        repository.getObservableBusStopsByDepartureBusStopName(arrivalBusStopNameValue)
    }

    val arrivalBusStopName = Transformations.map(arrivalBusStops) { arrivalBusStopsValue ->
        if (arrivalBusStopsValue.isEmpty()) {
            return@map null
        }

        arrivalBusStopsValue[0.until(arrivalBusStopsValue.size).random()].name
    }

    val routes = MediatorLiveData<List<Route>>().apply {
        var source: LiveData<List<Route>>? = null

        fun update() {
            val departureBusStopNameValue = departureBusStopName.value ?: return
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopNameValue, arrivalBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(departureBusStopName) { update() }
        addSource(arrivalBusStopName)   { update() }
    }

    val routeNames = Transformations.map(routes) { routesValue ->
        routesValue.map { it.name }.joinToString("\n")
    }
}
