package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RouteBusStopPoleDao {
    @Insert
    fun add(routeBusStopPole: RouteBusStopPole): Long

    @Query("DELETE FROM RouteBusStopPole")
    fun clear()

    @Query(
        """
            SELECT RouteBusStopPole.*
            FROM RouteBusStopPole
            INNER JOIN (
                SELECT RouteBusStopPole.routeId, RouteBusStopPole.'order'
                FROM RouteBusStopPole
                INNER JOIN BusStopPole ON BusStopPole.id = RouteBusStopPole.busStopPoleId
                WHERE RouteBusStopPole.routeId IN (:routeIds) AND BusStopPole.busStopName = :departureBusStopName
            ) DepartureRouteBusStopPole ON RouteBusStopPole.routeId = DepartureRouteBusStopPole.routeId
            WHERE RouteBusStopPole.'order' <= DepartureRouteBusStopPole.'order' AND RouteBusStopPole.'order' >= DepartureRouteBusStopPole.'order' - 10
        """
    )
    fun getObservablesByRouteIdsAndDepartureBusStopName(routeIds: List<String>, departureBusStopName: String): LiveData<List<RouteBusStopPole>>
}
