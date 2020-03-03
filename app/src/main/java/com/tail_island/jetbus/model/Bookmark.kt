package com.tail_island.jetbus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    var departureBusStopName: String,

    var arrivalBusStopName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
