package com.tail_island.jetbus

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.tail_island.jetbus.model.AppDatabase
import com.tail_island.jetbus.model.Repository
import com.tail_island.jetbus.model.WebService
import com.tail_island.jetbus.view_model.*
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideContext() = application as Context

    @Provides
    @Singleton
    @Named("consumerKey")
    fun provideConsumerKey(context: Context) = context.getString(R.string.consumerKey)

    @Provides
    @Singleton
    fun provideDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "jetbus.db").build()

    @Provides
    @Singleton
    fun provideWebService() = Retrofit.Builder().apply {
        baseUrl("https://api.odpt.org")
        client(
            OkHttpClient.Builder().apply {
                connectTimeout(180, TimeUnit.SECONDS)
                readTimeout(180, TimeUnit.SECONDS)
                writeTimeout(180, TimeUnit.SECONDS)
            }.build()
        )
        addConverterFactory(GsonConverterFactory.create())
    }.build().create(WebService::class.java)

    @Provides
    @IntoMap
    @ViewModelKey(ArrivalBusStopViewModel::class)
    fun provideArrivalBusStopViewModel(repository: Repository) = ArrivalBusStopViewModel(repository) as ViewModel

    @Provides
    @IntoMap
    @ViewModelKey(BookmarksViewModel::class)
    fun provideBookmarksViewModel(repository: Repository) = BookmarksViewModel(repository) as ViewModel

    @Provides
    @IntoMap
    @ViewModelKey(BusApproachesViewModel::class)
    fun provideBusApproachesViewModel(repository: Repository) = BusApproachesViewModel(repository) as ViewModel

    @Provides
    @IntoMap
    @ViewModelKey(DepartureBusStopViewModel::class)
    fun provideDepartureBusStopViewModel(repository: Repository) = DepartureBusStopViewModel(repository) as ViewModel

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(repository: Repository) = MainViewModel(repository) as ViewModel

    @Provides
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideSplashViewModel(repository: Repository) = SplashViewModel(repository) as ViewModel
}
