package com.example.beercounter.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.beercounter.database.entities.Beers

@Dao
interface BeersDao {

    @Insert
    fun insert(beer: Beers)

    @Query("SELECT * FROM beers WHERE session_id = :sessionId")
    fun getBySessionId(sessionId: Long?): LiveData<List<Beers>>

    @Query("SELECT * FROM beers")
    fun getAll(): LiveData<List<Beers>>
}