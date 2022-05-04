package com.example.badpostfilter.database

import androidx.room.*

@Dao
interface ThoughtDao {
    @Insert
    fun addThought(thought : Thought) : Long

    @Update
    fun updateThought(thought: Thought)

    @Query("SELECT * FROM thought")
    fun getAllThoughts() : List<Thought>

    @Query("SELECT * FROM thought where id = :thoughtId")
    fun getThought(thoughtId : Long) : Thought

    @Delete
    fun deleteThought(thought: Thought)
}