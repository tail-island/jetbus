package com.tail_island.jetbus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = TimeTable::class, parentColumns = ["id"], childColumns = ["timeTableId"]), ForeignKey(entity = BusStopPole::class, parentColumns = ["id"], childColumns = ["busStopPoleId"])])
data class TimeTableDetail(
    @ColumnInfo(index = true)
    var timeTableId: String,

    val order: Int,

    @ColumnInfo(index = true)
    var busStopPoleId: String,

    var arrival: Int  // 00:00:00からの秒数です。翌日未明のデータ（24:53とか25:38とか）があるので、LocalTimeでは表現できませんでした……
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
