package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentSplashBinding
import com.tail_island.jetbus.model.WebService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import kotlin.concurrent.thread

class SplashFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        val consumerKey = getString(R.string.consumerKey)

        val webService = Retrofit.Builder().apply {
            baseUrl("https://api.odpt.org")
            addConverterFactory(GsonConverterFactory.create())
        }.build().create(WebService::class.java)

        thread {
            try {
                getWebServiceResultBody { webService.busstopPole(consumerKey) }?.let { busStopPoleJsonArray ->
                    for (busStopPoleJsonObject in busStopPoleJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }.take(10)) {
                        Log.d("SplashFragment", "${busStopPoleJsonObject.get("owl:sameAs")}")
                        Log.d("SplashFragment", "${busStopPoleJsonObject.get("dc:title")}")
                        Log.d("SplashFragment", "${busStopPoleJsonObject.get("odpt:kana")}")
                    }
                }

                getWebServiceResultBody { webService.busroutePattern(consumerKey) }?.let { busroutePatternJsonArray ->
                    for (busroutePatternJsonObject in busroutePatternJsonArray.map { it.asJsonObject }.filter { it.get("odpt:operator").asString == "odpt.Operator:Toei" }.take(10)) {
                        Log.d("SplashFragment", "${busroutePatternJsonObject.get("owl:sameAs")}")
                        Log.d("SplashFragment", "${busroutePatternJsonObject.get("dc:title")}")

                        for (busstopPoleOrderJsonObject in busroutePatternJsonObject.get("odpt:busstopPoleOrder").asJsonArray.take(10).map { it.asJsonObject }) {
                            Log.d("SplashFragment", "${busstopPoleOrderJsonObject.get("odpt:index")}")
                            Log.d("SplashFragment", "${busstopPoleOrderJsonObject.get("odpt:busstopPole")}")
                        }

                        getWebServiceResultBody { webService.bus(consumerKey, busroutePatternJsonObject.get("owl:sameAs").asString) }?.let { buses ->
                            for (bus in buses.take(10)) {
                                Log.d("SplashFragment", bus.id)
                                Log.d("SplashFragment", bus.routeId)
                                Log.d("SplashFragment", bus.fromBusStopPoleId)
                            }
                        }
                    }
                }

            } catch (e: IOException) {
                Log.e("SplashFragment", "${e.message}")
            }
        }
    }
}
