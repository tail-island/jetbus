package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RouteDao {
    @Insert
    fun add(route: Route)

    @Query("DELETE FROM Route")
    fun clear()

    @Query(
        """
            SELECT Route.*
            FROM BusStopPole AS ArrivalBusStopPole
            INNER JOIN RouteBusStopPole AS ArrivalRouteBusStopPole ON ArrivalRouteBusStopPole.busStopPoleId = ArrivalBusStopPole.id 
            INNER JOIN Route ON Route.id = ArrivalRouteBusStopPole.routeId
            INNER JOIN RouteBusStopPole As DepartureRouteBusStopPole ON DepartureRouteBusStopPole.routeId = Route.id
            INNER JOIN BusStopPole AS DepartureBusStopPole ON DepartureBusStopPole.id = DepartureRouteBusStopPole.busStopPoleId
            WHERE ArrivalRouteBusStopPole.'order' > DepartureRouteBusStopPole.'order' AND DepartureBusStopPole.busStopName = :departureBusStopName AND ArrivalBusStopPole.busStopName = :arrivalBusStopName
        """
    )
    fun getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String): LiveData<List<Route>>
}
