package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BusStopPoleDao {
    @Insert
    fun add(busStopPole: BusStopPole)

    @Query("DELETE FROM BusStopPole")
    fun clear()

    @Query("SELECT * FROM BusStopPole WHERE id IN (:ids)")
    fun getObservablesByIds(ids: List<String>): LiveData<List<BusStopPole>>
}
