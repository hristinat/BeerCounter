package com.example.beercounter.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.entities.Beers
import com.example.beercounter.database.entities.Session

class CalendarViewModel(database: BeerCounterDatabase) : ViewModel() {

    var sessions = database.sessionDao.getAll()
    var beers = database.beersDao.getAll()

    val mediatorLiveData = MediatorLiveData<Pair<List<Beers>?, List<Session>?>>()

    init {
        mediatorLiveData.addSource(beers) {
            mediatorLiveData.value = combineLatestData(beers, sessions)
        }

        mediatorLiveData.addSource(sessions) {
            mediatorLiveData.value = combineLatestData(beers, sessions)
        }
    }

    private fun combineLatestData(
        beersLiveData: LiveData<List<Beers>>,
        sessionsLiveData: LiveData<List<Session>>
    ): Pair<List<Beers>?, List<Session>?>? {

        val beers = beersLiveData.value
        val sessions = sessionsLiveData.value

        // Don't send a success until we have both results
        if (beers == null || sessions == null) {
            return null
        }

        // TODO: Check for errors and return UserDataError if any.

        return Pair(beers, sessions)
    }
}