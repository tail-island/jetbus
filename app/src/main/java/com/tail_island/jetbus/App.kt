package com.tail_island.jetbus

import android.app.Application

class App: Application() {
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder().apply {
            appModule(AppModule(this@App))
        }.build()
    }
}