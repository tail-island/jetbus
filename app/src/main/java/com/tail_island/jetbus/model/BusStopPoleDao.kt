package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BusStopPoleDao {
    @Insert
    fun add(busStopPole: BusStopPole)

    @Query("DELETE FROM BusStopPole")
    fun clear()
}
