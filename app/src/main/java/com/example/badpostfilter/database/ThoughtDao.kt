package com.example.badpostfilter.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ThoughtDao {
    @Query("SELECT * FROM thought")
    fun getAllThoughts() : List<Thought>
}