package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RouteBusStopPoleDao {
    @Insert
    fun add(routeBusStopPole: RouteBusStopPole): Long

    @Query("DELETE FROM RouteBusStopPole")
    fun clear()
}
