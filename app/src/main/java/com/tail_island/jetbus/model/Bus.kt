package com.tail_island.jetbus.model

import com.google.gson.annotations.SerializedName

data class Bus(
    @SerializedName("owl:sameAs")
    var id: String,

    @SerializedName("odpt:busroutePattern")
    var routeId: String,

    @SerializedName("odpt:fromBusstopPole")
    var fromBusStopPoleId: String
)
