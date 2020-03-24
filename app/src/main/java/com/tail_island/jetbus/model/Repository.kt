package com.tail_island.jetbus.model

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val database: AppDatabase, private val webService: WebService, @param:Named("consumerKey") private val consumerKey: String) {
    suspend fun clearDatabase() = withContext(Dispatchers.IO) {
        try {
            database.withTransaction {
                database.getTimeTableDetailDao().clear()
                database.getTimeTableDao().clear()
                database.getRouteBusStopPoleDao().clear()
                database.getRouteDao().clear()
                database.getBusStopPoleDao().clear()
                database.getBusStopDao().clear()
            }

            Unit

        } catch (e: IOException) {
            Log.e("Repository", "${e.message}")
            null
        }
    }

    private fun <T> getWebServiceResultBody(callWebService: () -> Call<T>): T? {
        val response = callWebService().execute()

        if (!response.isSuccessful) {
            Log.e("Repository", "HTTP Error: ${response.code()}")
            return null
        }

        return response.body()
    }

    suspend fun syncDatabase() = withContext(Dispatchers.IO) {
        try {
            if (database.getBusStopDao().getCount() > 0) {
                return@withContext Unit
            }

            val busStopPoleJsonArray = getWebServiceResultBody { webService.busstopPole(consumerKey)     } ?: return@withContext null
            val routeJsonArray =       getWebServiceResultBody { webService.busroutePattern(consumerKey) } ?: return@withContext null

            database.withTransaction {
                for (busStopPoleJsonObject in busStopPoleJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }) {
                    val busStop = database.getBusStopDao().getByName(busStopPoleJsonObject.get("dc:title").asString) ?: run {
                        BusStop(
                            busStopPoleJsonObject.get("dc:title").asString,
                            busStopPoleJsonObject.get("odpt:kana")?.asString
                        ).also {
                            database.getBusStopDao().add(it)
                        }
                    }

                    BusStopPole(
                        busStopPoleJsonObject.get("owl:sameAs").asString,
                        busStop.name
                    ).also {
                        database.getBusStopPoleDao().add(it)
                    }
                }

                for (routeJsonObject in routeJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }) {
                    val route = Route(
                        routeJsonObject.get("owl:sameAs").asString,
                        routeJsonObject.get("dc:title").asString
                    ).also {
                        database.getRouteDao().add(it)
                    }

                    for (routeBusStopPoleJsonObject in routeJsonObject.get("odpt:busstopPoleOrder").asJsonArray.map { it.asJsonObject }) {
                        RouteBusStopPole(
                            route.id,
                            routeBusStopPoleJsonObject.get("odpt:index").asInt,
                            routeBusStopPoleJsonObject.get("odpt:busstopPole").asString
                        ).also {
                            it.id = database.getRouteBusStopPoleDao().add(it)
                        }
                    }
                }
            }

            Unit

        } catch (e: IOException) {
            Log.e("Repository", "${e.message}")
            null
        }
    }

    suspend fun toggleBookmark(departureBusStopName: String, arrivalBusStopName: String) = withContext(Dispatchers.IO) {
        val bookmark = database.getBookmarkDao().get(departureBusStopName, arrivalBusStopName)

        if (bookmark == null) {
            database.getBookmarkDao().add(Bookmark(departureBusStopName, arrivalBusStopName))
        } else {
            database.getBookmarkDao().remove(bookmark)
        }
    }

    fun getObservableBookmarks() = database.getBookmarkDao().getObservables()
    fun getObservableBookmarkByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String) = database.getBookmarkDao().getObservableByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName, arrivalBusStopName)
    fun getObservableBusStops() = database.getBusStopDao().getObservables()
    fun getObservableBusStopsByDepartureBusStopName(departureBusStopName: String) = database.getBusStopDao().getObservablesByDepartureBusStopName(departureBusStopName)
    fun getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String) = database.getRouteDao().getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName, arrivalBusStopName)
}
