package com.example.beercounter.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.entities.Session
import com.example.beercounter.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SessionDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: BeerCounterDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            BeerCounterDatabase::class.java
        ).build()
    }

    @After
    fun clean() = database.close()

    @Test
    fun testInsertAndGetLastSession() {
        val session = Session()
        database.sessionDao.insert(session)

        val loaded = database.sessionDao.getLastSession()!!

        assertThat(loaded.date, `is`(session.date))
        assertThat(loaded.isFinished, `is`(session.isFinished))
    }

    @Test
    fun testInsertAndGetAll() {
        val session = Session()
        val secondSession = Session()

        database.sessionDao.insert(session)
        database.sessionDao.insert(secondSession)

        val loaded = database.sessionDao.getAll().getOrAwaitValue()

        assertThat(loaded.size, `is`(2))
    }

    @Test
    fun testUpdateAndGetLastSession() {
        var session = Session()

        database.sessionDao.insert(session)
        session = database.sessionDao.getLastSession()!!

        val updatedSession = session.apply { isFinished = true }
        database.sessionDao.update(updatedSession)

        val loaded = database.sessionDao.getLastSession()!!

        assertThat(loaded.isFinished, `is`(true))
        assertThat(loaded.date, `is`(session.date))
    }
}