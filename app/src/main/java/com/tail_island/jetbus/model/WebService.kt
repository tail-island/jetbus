package com.tail_island.jetbus.model

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WebService {
    @GET("/api/v4/odpt:BusstopPole.json")
    fun busstopPole(@Query("acl:consumerKey") consumerKey: String): Call<JsonArray>

    @GET("/api/v4/odpt:BusroutePattern.json")
    fun busroutePattern(@Query("acl:consumerKey") consumerKey: String): Call<JsonArray>

    @GET("/api/v4/odpt:BusTimetable")
    fun busTimeTable(@Query("acl:consumerKey") consumerKey: String, @Query("odpt:busroutePattern") routePattern: String): Call<JsonArray>

    @GET("/api/v4/odpt:Bus")
    fun bus(@Query("acl:consumerKey") consumerKey: String, @Query("odpt:busroutePattern") routePattern: String): Call<List<Bus>>
}
