package com.tail_island.jetbus.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Route(
    @PrimaryKey
    var id: String,

    var name: String
)
