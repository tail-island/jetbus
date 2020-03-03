package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RouteDao {
    @Insert
    fun add(route: Route)

    @Query("DELETE FROM Route")
    fun clear()
}
