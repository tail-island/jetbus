package com.tail_island.jetbus.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface BookmarkDao {
    @Insert
    fun add(bookmark: Bookmark)

    @Delete
    fun remove(bookmark: Bookmark)
}
