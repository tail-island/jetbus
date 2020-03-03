package com.tail_island.jetbus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BusStop(
    @PrimaryKey
    var name: String,

    var phoneticName: String?
)
