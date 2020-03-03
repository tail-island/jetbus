package com.tail_island.jetbus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = BusStop::class, parentColumns = ["name"], childColumns = ["busStopName"])])
data class BusStopPole(
    @PrimaryKey
    var id: String,

    @ColumnInfo(index = true)
    var busStopName: String
)
