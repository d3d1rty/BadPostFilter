package com.example.badpostfilter.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ThoughtDao {
    @Insert
    fun addThought(thought : Thought) : Long

    @Query("SELECT * FROM thought")
    fun getAllThoughts() : List<Thought>
}