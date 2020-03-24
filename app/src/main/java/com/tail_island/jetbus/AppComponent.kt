package com.tail_island.jetbus

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(arrivalBusStopFragment: ArrivalBusStopFragment)
    fun inject(bookmarksFragment: BookmarksFragment)
    fun inject(busApproachesFragment: BusApproachesFragment)
    fun inject(departureBusStopFragment: DepartureBusStopFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(splashFragment: SplashFragment)
}
