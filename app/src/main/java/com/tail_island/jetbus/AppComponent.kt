package com.tail_island.jetbus

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(busApproachesFragment: BusApproachesFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(splashFragment: SplashFragment)
}
