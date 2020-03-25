package com.tail_island.jetbus.model

import android.util.Log
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.IOException
import java.util.*
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

    suspend fun syncTimeTables(routes: Iterable<Route>) = withContext(Dispatchers.IO) {
        try {
            for (route in routes) {
                delay(0)  // 一応だけど、キャンセル可能にしてみました。。。

                if (database.getTimeTableDao().getCountByRouteId(route.id) > 0) {
                    continue
                }

                val timeTableJsonArray = getWebServiceResultBody { webService.busTimeTable(consumerKey, route.id) } ?: return@withContext null

                database.withTransaction {
                    for (timeTableJsonObject in timeTableJsonArray.map { it.asJsonObject }) {
                        val timeTable = TimeTable(
                            timeTableJsonObject.get("owl:sameAs").asString,
                            timeTableJsonObject.get("odpt:busroutePattern").asString
                        ).also {
                            database.getTimeTableDao().add(it)
                        }

                        for (timeTableDetailJsonObject in timeTableJsonObject.get("odpt:busTimetableObject").asJsonArray.map { it.asJsonObject }) {
                            TimeTableDetail(
                                timeTable.id,
                                timeTableDetailJsonObject.get("odpt:index").asInt,
                                timeTableDetailJsonObject.get("odpt:busstopPole").asString,
                                timeTableDetailJsonObject.get("odpt:arrivalTime").asString.split(":").let { (hour, minute) -> hour.toInt() * 60 * 60 + minute.toInt() * 60 }
                            ).also {
                                it.id = database.getTimeTableDetailDao().add(it)
                            }
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

    suspend fun clearBookmarks() = withContext(Dispatchers.IO) {
        database.getBookmarkDao().clear()
    }

    suspend fun toggleBookmark(departureBusStopName: String, arrivalBusStopName: String) = withContext(Dispatchers.IO) {
        val bookmark = database.getBookmarkDao().get(departureBusStopName, arrivalBusStopName)

        if (bookmark == null) {
            database.getBookmarkDao().add(Bookmark(departureBusStopName, arrivalBusStopName))
        } else {
            database.getBookmarkDao().remove(bookmark)
        }
    }

    suspend fun getBuses(routes: Iterable<Route>, routeBusStopPoles: Iterable<RouteBusStopPole>) = withContext(Dispatchers.IO) {
        try {
            val busStopPoleIds = routeBusStopPoles.groupBy { it.routeId }.map { (routeId, routeBusStopPoles) -> Pair(routeId, routeBusStopPoles.map { it.busStopPoleId }.toSet()) }.toMap()

            getWebServiceResultBody { webService.bus(consumerKey, routes.map { it.id }.joinToString(",")) }?.filter { bus ->
                // routeBusStopPolesに含まれるバス停を出発したところ、かつ、routeBusStopPolesの同じルートの最後（つまり出発バス停）を出発したのではない
                bus.fromBusStopPoleId in busStopPoleIds.getValue(bus.routeId) && bus.fromBusStopPoleId != routeBusStopPoles.filter { it.routeId == bus.routeId }.sortedByDescending { it.order }.first().busStopPoleId
            }

        } catch (e: IOException) {
            Log.e("Repository", "${e.message}")
            null
        }
    }

    fun getObservableBookmarks() = database.getBookmarkDao().getObservables()
    fun getObservableBookmarkByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String) = database.getBookmarkDao().getObservableByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName, arrivalBusStopName)
    fun getObservableBusStopPolesByRouteBusStopPoles(routeBusStopPoles: Iterable<RouteBusStopPole>) = database.getBusStopPoleDao().getObservablesByIds(routeBusStopPoles.map { it.busStopPoleId }.distinct())  // 複数の路線が同じバス停を含んでいる可能性があるので、distinct()しておきます。
    fun getObservableBusStops() = database.getBusStopDao().getObservables()
    fun getObservableBusStopsByDepartureBusStopName(departureBusStopName: String) = database.getBusStopDao().getObservablesByDepartureBusStopName(departureBusStopName)
    fun getObservableRouteBusStopPolesByRoutes(routes: Iterable<Route>, departureBusStopName: String) = database.getRouteBusStopPoleDao().getObservablesByRouteIdsAndDepartureBusStopName(routes.map { it.id }, departureBusStopName)
    fun getObservableRoutesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName: String, arrivalBusStopName: String) = database.getRouteDao().getObservablesByDepartureBusStopNameAndArrivalBusStopName(departureBusStopName, arrivalBusStopName)
    fun getObservableTimeTablesByRoutesAndDepartureBusStop(routes: Iterable<Route>, departureBusStopName: String) = database.getTimeTableDao().getObservablesByRouteIdsAndDepartureBusStopName(routes.map { it.id }, departureBusStopName, Calendar.getInstance().let { it.get(Calendar.HOUR_OF_DAY) * 60 * 60 + it.get(Calendar.MINUTE) * 60 })
    fun getObservableTimeTableDetailsByTimeTablesAndBusStopPoles(timeTables: Iterable<TimeTable>, busStopPoles: Iterable<BusStopPole>) = database.getTimeTableDetailDao().getObservablesByTimeTableIdsAndBusStopPoleIds(timeTables.map { it.id }, busStopPoles.map { it.id })
}
