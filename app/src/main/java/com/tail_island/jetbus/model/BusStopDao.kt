package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BusStopDao {
    @Insert
    fun add(busStop: BusStop)

    @Query("DELETE FROM BusStop")
    fun clear()

    @Query("SELECT * FROM BusStop WHERE name = :name LIMIT 1")
    fun getByName(name: String): BusStop?

    @Query("SELECT COUNT(*) FROM BusStop")
    fun getCount(): Int

    @Query("SELECT * FROM BusStop ORDER BY phoneticName")
    fun getObservables(): LiveData<List<BusStop>>

    @Query(
        """
            SELECT DISTINCT ArrivalBusStop.*
            FROM BusStop AS ArrivalBusStop
            INNER JOIN BusStopPole AS ArrivalBusStopPole ON ArrivalBusStopPole.busStopName = ArrivalBusStop.name
            INNER JOIN RouteBusStopPole AS ArrivalRouteBusStopPole ON ArrivalRouteBusStopPole.busStopPoleId = ArrivalBusStopPole.id
            INNER JOIN Route ON Route.id = ArrivalRouteBusStopPole.routeId
            INNER JOIN RouteBusStopPole AS DepartureRouteBusStopPole ON DepartureRouteBusStopPole.routeId = Route.id
            INNER JOIN BusStopPole AS DepartureBusStopPole ON DepartureBusStopPole.id = DepartureRouteBusStopPole.busStopPoleId
            INNER JOIN BusStop AS DepartureBusStop ON DepartureBusStop.name = DepartureBusStopPole.busStopName
            WHERE DepartureBusStop.name = :departureBusStopName AND ArrivalBusStop.name <> :departureBusStopName
            ORDER BY ArrivalBusStop.phoneticName
        """
    )
    fun getObservablesByDepartureBusStopName(departureBusStopName: String): LiveData<List<BusStop>>
}
