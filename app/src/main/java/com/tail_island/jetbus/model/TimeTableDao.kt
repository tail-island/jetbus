package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeTableDao {
    @Insert
    fun add(timeTable: TimeTable)

    @Query("DELETE FROM TimeTable")
    fun clear()

    @Query("SELECT COUNT(*) FROM TimeTable WHERE routeId = :routeId")
    fun getCountByRouteId(routeId: String): Int

    @Query(
        """
            SELECT TimeTable.*
            FROM TimeTable
            INNER JOIN (
                SELECT TimeTable.routeId, MIN(TimeTable.id) AS id
                FROM TimeTable
                INNER JOIN TimeTableDetail ON TimeTableDetail.timeTableId = TimeTable.id
                INNER JOIN BusStopPole ON BusStopPole.id = TimeTableDetail.busStopPoleId
                WHERE
                    TimeTable.routeId IN (:routeIds) AND
                    BusStopPole.busStopName = :departureBusStopName AND
                    NOT EXISTS (
                        SELECT T1.*
                        FROM TimeTable AS T1
                        INNER JOIN TimeTableDetail AS T2 ON T2.timeTableId = T1.id
                        INNER JOIN BusStopPole AS T3 ON T3.id = T2.busStopPoleId
                        WHERE T1.routeId = TimeTable.routeId AND T3.busStopName = BusStopPole.busStopName AND ABS(T2.arrival - :now) < ABS(TimeTableDetail.arrival - :now)
                    )
                GROUP BY TimeTable.routeId
            ) AS T ON T.id = TimeTable.id
        """)
    fun getObservablesByRouteIdsAndDepartureBusStopName(routeIds: List<String>, departureBusStopName: String, now: Int): LiveData<List<TimeTable>>
}
