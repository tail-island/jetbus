package com.tail_island.jetbus

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(splashFragment: SplashFragment)
    fun inject(busApproachesFragment: BusApproachesFragment)
}
