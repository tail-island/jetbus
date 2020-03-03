package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeTableDetailDao {
    @Insert
    fun add(timeTableDetail: TimeTableDetail): Long

    @Query("DELETE FROM TimeTableDetail")
    fun clear()
}
