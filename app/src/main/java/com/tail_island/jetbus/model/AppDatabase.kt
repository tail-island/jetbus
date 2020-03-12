package com.tail_island.jetbus.model

import androidx.room.Database
import androidx.room.RoomDatabase
import javax.inject.Singleton

@Singleton
@Database(entities = [Bookmark::class, BusStop::class, BusStopPole::class, Route::class, RouteBusStopPole::class, TimeTable::class, TimeTableDetail::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getBookmarkDao(): BookmarkDao
    abstract fun getBusStopDao(): BusStopDao
    abstract fun getBusStopPoleDao(): BusStopPoleDao
    abstract fun getRouteBusStopPoleDao(): RouteBusStopPoleDao
    abstract fun getRouteDao(): RouteDao
    abstract fun getTimeTableDao(): TimeTableDao
    abstract fun getTimeTableDetailDao(): TimeTableDetailDao
}
