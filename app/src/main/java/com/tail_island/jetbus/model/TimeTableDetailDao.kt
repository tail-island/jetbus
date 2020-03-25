package com.tail_island.jetbus.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeTableDetailDao {
    @Insert
    fun add(timeTableDetail: TimeTableDetail): Long

    @Query("DELETE FROM TimeTableDetail")
    fun clear()

    @Query("SELECT * FROM TimeTableDetail WHERE timeTableId IN (:timeTableIds) AND busStopPoleId IN (:busStopPoleIds) ORDER BY 'order'")
    fun getObservablesByTimeTableIdsAndBusStopPoleIds(timeTableIds: List<String>, busStopPoleIds: List<String>): LiveData<List<TimeTableDetail>>
}
