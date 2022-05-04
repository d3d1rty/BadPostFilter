package com.example.badpostfilter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Thought::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun thoughtDao() : ThoughtDao

    companion object {
        private val NAME_OF_DATABASE = "thoughts"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        NAME_OF_DATABASE
                    ).build()
                }
            }

            return INSTANCE!!
        }
    }

}