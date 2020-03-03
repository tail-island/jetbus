package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeTableDao {
    @Insert
    fun add(timeTable: TimeTable)

    @Query("DELETE FROM TimeTable")
    fun clear()
}
