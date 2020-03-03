package com.tail_island.jetbus.model

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
}
