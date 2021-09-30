package com.example.beercounter.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.entities.Beers
import com.example.beercounter.database.entities.Session

class StatisticsViewModel(val database: BeerCounterDatabase) : ViewModel() {

    var beers = database.beersDao.getAll()

    var sessions = database.sessionDao.getAll()

    var liters: LiveData<Double> = Transformations.map(beers) { beers ->
        beersToLiters(beers)
    }

    val beersSessions = MediatorLiveData<Pair<List<Beers>?, List<Session>?>>()

//    fun getBeersSessions():CombinedSubjectReminders? {
//        var beersLiveData = beers
//        var sessionsLivedata = sessions
//
//        if (beersLiveData != null && sessionsLivedata != null) {
//            return CombinedSubjectReminders(beersLiveData, sessionsLivedata)
//        }
//        return null
//    }

    init {
        beersSessions.addSource(beers) {
            beersSessions.value = combineLatestData(beers, sessions)
        }

        beersSessions.addSource(sessions) {
            beersSessions.value = combineLatestData(beers, sessions)
        }
    }

    private fun combineLatestData(
        beersLiveData: LiveData<List<Beers>>,
        sessionsLivedata: LiveData<List<Session>>
    ): Pair<List<Beers>?, List<Session>?>? {

        val beers = beersLiveData.value
        val sessions = sessionsLivedata.value

        // Don't send a success until we have both results
        if (beers == null || sessions == null) {
            return null
        }

        // TODO: Check for errors and return UserDataError if any.

        return Pair(beers, sessions)
    }
}

class CombinedSubjectReminders(
    beersLiveData: LiveData<List<Beers>>,
    sessionsLivedata: LiveData<List<Session>>
) : MediatorLiveData<Pair<List<Beers>, List<Session>>>() {

    private var beers: List<Beers> = emptyList()
    private var sessions: List<Session> = emptyList()

    init {
        value = Pair(beers, sessions)

        addSource(beersLiveData) {
            if( it != null ) beers = it
            value = Pair(beers, sessions)
        }

        addSource(sessionsLivedata) {
            if( it != null ) sessions = it
            value = Pair(beers, sessions)
        }
    }
}