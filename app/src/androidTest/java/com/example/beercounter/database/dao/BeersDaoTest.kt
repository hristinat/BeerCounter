package com.example.beercounter.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.BeerType
import com.example.beercounter.database.entities.Beers
import com.example.beercounter.database.entities.Session
import com.example.beercounter.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BeersDaoTest {

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
    fun testInsertAndGetBySessionId() {

        // TODO: check if only one dao could be tested
        var session = Session()
        database.sessionDao.insert(session)
        session = database.sessionDao.getLastSession()!!
        val beer = Beers(type = BeerType.LITER, sessionId = session.id)
        database.beersDao.insert(beer)

        val loaded = database.beersDao.getBySessionId(session.id).getOrAwaitValue()[0]

        assertThat(loaded.type, `is`(beer.type))
        assertThat(loaded.count, `is`(beer.count))
        assertThat(loaded.sessionId, `is`(beer.sessionId))
    }

    @Test
    fun testGetAllDifferentSession() {
        var session = Session()
        database.sessionDao.insert(session)
        session = database.sessionDao.getLastSession()!!
        val beerLiter = Beers(type = BeerType.LITER, sessionId = session.id)
        val beerThird = Beers(type = BeerType.THIRD, sessionId = session.id)
        database.beersDao.insert(beerLiter)
        database.beersDao.insert(beerThird)

        var newSession = Session()
        database.sessionDao.insert(newSession)
        newSession = database.sessionDao.getLastSession()!!
        val beerLiterNewSession = Beers(type = BeerType.LITER, sessionId = newSession.id)
        database.beersDao.insert(beerLiterNewSession)

        val loaded = database.beersDao.getAll().getOrAwaitValue()

        assertThat(loaded.size, `is`(3))
        assertThat(loaded.filter { it.sessionId == session.id }.size, `is`(2))
        assertThat(loaded.filter { it.sessionId == newSession.id }.size, `is`(1))
    }
}