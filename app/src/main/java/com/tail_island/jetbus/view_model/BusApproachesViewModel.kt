package com.tail_island.jetbus.view_model

import androidx.lifecycle.*
import com.tail_island.jetbus.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
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

    val routeBusStopPoles = MediatorLiveData<List<RouteBusStopPole>>().apply {
        var source: LiveData<List<RouteBusStopPole>>? = null

        fun update() {
            val routesValue               = routes.value               ?: return
            val departureBusStopNameValue = departureBusStopName.value ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableRouteBusStopPolesByRoutes(routesValue, departureBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(routes)               { update() }
        addSource(departureBusStopName) { update() }
    }

    val busStopPoles = Transformations.switchMap(routeBusStopPoles) { routeStopPolesValue ->
        repository.getObservableBusStopPolesByRouteBusStopPoles(routeStopPolesValue)
    }

    val timeTables = MediatorLiveData<List<TimeTable>>().apply {
        var source: LiveData<List<TimeTable>>? = null
        var job: Job? = null

        fun update() = viewModelScope.launch {  // Jobの管理をしたいので、launchします
            val routesValue               = routes.value               ?: return@launch
            val departureBusStopNameValue = departureBusStopName.value ?: return@launch

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableTimeTablesByRoutesAndDepartureBusStop(routesValue, departureBusStopNameValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }

            job?.cancelAndJoin()

            job = viewModelScope.launch {
                repository.syncTimeTables(routesValue)
            }
        }

        addSource(routes)               { update() }
        addSource(departureBusStopName) { update() }
    }

    val timeTableDetails = MediatorLiveData<List<TimeTableDetail>>().apply {
        var source: LiveData<List<TimeTableDetail>>? = null

        fun update() {
            val timeTablesValue   = timeTables.value   ?: return
            val busStopPolesValue = busStopPoles.value ?: return

            source?.let {
                removeSource(it)
            }

            source = repository.getObservableTimeTableDetailsByTimeTablesAndBusStopPoles(timeTablesValue, busStopPolesValue).also {
                addSource(it) { sourceValue ->
                    value = sourceValue
                }
            }
        }

        addSource(timeTables)   { update() }
        addSource(busStopPoles) { update() }
    }

    val buses = MediatorLiveData<List<Bus>>().apply {
        var job: Job? = null

        fun update() = viewModelScope.launch {  // Jobの管理をしたいので、launchします
            val routesValue            = routes.value            ?: return@launch
            val routeBusStopPolesValue = routeBusStopPoles.value ?: return@launch

            job?.cancelAndJoin()

            job = viewModelScope.launch {
                while (true) {
                    value = repository.getBuses(routesValue, routeBusStopPolesValue) ?: listOf()

                    delay(15000)
                }
            }
        }

        addSource(routes)            { update() }
        addSource(routeBusStopPoles) { update() }
    }

    val busApproaches = MediatorLiveData<List<BusApproach>>().apply {
        fun update() {
            val routesValue            = routes.value            ?: return
            val routeBusStopPolesValue = routeBusStopPoles.value ?: return
            val busStopPolesValue      = busStopPoles.value      ?: return
            val timeTablesValue        = timeTables.value        // syncTimeTable()は時間がかかるので、
            val timeTableDetailsValue  = timeTableDetails.value  // 時刻表がない場合はバス停の数で代用することにしてnullでも強行します。
            val busesValue             = buses.value             ?: return

            value = busesValue.map { bus ->
                BusApproach(
                    bus.id,
                    timeTablesValue?.find { timeTable -> timeTable.routeId == bus.routeId }?.let { timeTable ->
                        timeTableDetailsValue?.filter { it.timeTableId == timeTable.id }?.sortedByDescending { it.order }?.takeWhile { it.busStopPoleId != bus.fromBusStopPoleId }?.zipWithNext()?.map { (next, prev) -> next.arrival - prev.arrival }?.sum()
                    },
                    routeBusStopPolesValue.sortedByDescending { it.order }.let { it.first().order - it.find { routeBusStopPole -> routeBusStopPole.busStopPoleId == bus.fromBusStopPoleId }!!.order },
                    routesValue.find { it.id == bus.routeId }!!.name,
                    busStopPolesValue.find { it.id == bus.fromBusStopPoleId }!!.busStopName
                )
            }.sortedWith(compareBy({ it.willArriveAfter ?: Int.MAX_VALUE }, { it.busStopCount }))
        }

        addSource(routes)            { update() }
        addSource(routeBusStopPoles) { update() }
        addSource(busStopPoles)      { update() }
        addSource(timeTables)        { update() }
        addSource(timeTableDetails)  { update() }
        addSource(buses)             { update() }
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            val departureBusStopNameValue = departureBusStopName.value ?: return@launch
            val arrivalBusStopNameValue   = arrivalBusStopName.value   ?: return@launch

            repository.toggleBookmark(departureBusStopNameValue, arrivalBusStopNameValue)
        }
    }
}
