package com.example.beercounter.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.beercounter.database.BeerType

@Entity(tableName = "beers",
    foreignKeys = [ForeignKey(
        entity = Session::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("session_id"),
        onDelete = CASCADE
    )]
)
data class Beers(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val count: Int = 1,
    val type: BeerType,
    @ColumnInfo(name = "session_id")
    val sessionId: Long
)