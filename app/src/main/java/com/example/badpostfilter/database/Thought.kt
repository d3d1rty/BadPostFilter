package com.example.badpostfilter.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Thought(
    @PrimaryKey(autoGenerate = true) val id : Long?,
    @ColumnInfo val title : String,
    @ColumnInfo val thought : String,
    @ColumnInfo val approved : Boolean
) {
    override fun toString(): String {
        return title
    }
}