package com.example.beercounter.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Date = Calendar.getInstance().time,
    var isFinished: Boolean = false
)