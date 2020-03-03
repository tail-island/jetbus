package com.tail_island.jetbus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Route::class, parentColumns = ["id"], childColumns = ["routeId"])])
data class TimeTable(
    @PrimaryKey
    var id: String,

    @ColumnInfo(index = true)
    var routeId: String
)
