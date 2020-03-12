package com.tail_island.jetbus.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val database: AppDatabase) {
    fun getObservableBusStopsByDepartureBusStopName(departureBusStopName: String) = database.getBusStopDao().getObservablesByDepartureBusStopName(departureBusStopName)
    fun getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String) = database.getRouteDao().getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName, arrivalBusStopName)
}
