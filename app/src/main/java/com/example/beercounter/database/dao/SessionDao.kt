package com.example.beercounter.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.beercounter.database.entities.Session

@Dao
interface SessionDao {

    @Query("select * from session order by id desc limit 1")
    fun getLastSession(): Session?

    @Query("select * from session")
    fun getAll(): LiveData<List<Session>>

    @Insert
    fun insert(session: Session)

    @Update
    fun update(session: Session)
}