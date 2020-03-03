package com.tail_island.jetbus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Route::class, parentColumns = ["id"], childColumns = ["routeId"]), ForeignKey(entity = BusStopPole::class, parentColumns = ["id"], childColumns = ["busStopPoleId"])])
data class RouteBusStopPole(
    @ColumnInfo(index = true)
    var routeId: String,

    var order: Int,

    @ColumnInfo(index = true)
    var busStopPoleId: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
