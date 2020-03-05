package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentSplashBinding
import com.tail_island.jetbus.model.*
import retrofit2.Call
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named
import kotlin.concurrent.thread

class SplashFragment: Fragment() {
    @Inject @field:Named("consumerKey") lateinit var consumerKey: String
    @Inject lateinit var webService: WebService
    @Inject lateinit var database: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        return FragmentSplashBinding.inflate(inflater, container, false).apply {
            bookmarksButton.setOnClickListener {
                findNavController().navigate(SplashFragmentDirections.splashFragmentToBookmarksFragment())
            }
        }.root
    }

    private fun <T> getWebServiceResultBody(callWebService: () -> Call<T>): T? {
        val response = callWebService().execute()

        if (!response.isSuccessful) {
            Log.e("SplashFragment", "HTTP Error: ${response.code()}")
            return null
        }

        return response.body()
    }

    override fun onStart() {
        super.onStart()

        thread {
            try {
                Log.d("SplashFragment", "Start.")

                database.getTimeTableDetailDao().clear()
                database.getTimeTableDao().clear()
                database.getRouteBusStopPoleDao().clear()
                database.getRouteDao().clear()
                database.getBusStopPoleDao().clear()
                database.getBusStopDao().clear()

                val busStopPoleJsonArray = getWebServiceResultBody { webService.busstopPole(consumerKey)     } ?: return@thread
                val routeJsonArray       = getWebServiceResultBody { webService.busroutePattern(consumerKey) } ?: return@thread

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

                // Routeを登録します。
                for (routeJsonObject in routeJsonArray.map { it.asJsonObject}.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }) {
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

                Log.d("SplashFragment", "Finish.")

            } catch (e: IOException) {
                Log.e("SplashFragment", "${e.message}")
            }
        }
    }
}
