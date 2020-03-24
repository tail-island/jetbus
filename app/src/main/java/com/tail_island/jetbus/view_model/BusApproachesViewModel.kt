package com.tail_island.jetbus.view_model

import androidx.lifecycle.*
import com.tail_island.jetbus.model.Bookmark
import com.tail_island.jetbus.model.Repository
import com.tail_island.jetbus.model.Route
import kotlinx.coroutines.launch

class BusApproachesViewModel(private val repository: Repository): ViewModel() {
    val departureBusStopName = MutableLiveData<String>()

    val arrivalBusStopName = MutableLiveData<String>()

    val bookmark = MediatorLiveData<Bookmark?>().apply {
        var source: LiveData<Bookmark?>? = null

        fun update() {
            val departureBusStopNameValue = departureBusStopName.value ?: return
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableBookmarkByDepartureBusStopNameAndArrivalBusStopName(departureBusStopNameValue, arrivalBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(departureBusStopName) { update() }
        addSource(arrivalBusStopName)   { update() }
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

    fun toggleBookmark() {
        viewModelScope.launch {
            val departureBusStopNameValue = departureBusStopName.value ?: return@launch
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return@launch

            repository.toggleBookmark(departureBusStopNameValue, arrivalBusStopNameValue)
        }
    }
}
