package com.tail_island.jetbus.model

data class BusApproach(
    val id: String,
    val willArriveAfter: Int?,
    val busStopCount: Int,
    val routeName: String,
    val leftBusStopName: String
)
