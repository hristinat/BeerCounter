package com.example.beercounter.home

import android.util.Log
import androidx.lifecycle.*
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.dao.BeersDao
import com.example.beercounter.database.entities.Beers
import com.example.beercounter.database.entities.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val database: BeerCounterDatabase
) : ViewModel() {

    private val _navigateToAddBeer = MutableLiveData<Long>()
    val navigateToAddBeer: LiveData<Long>
        get() = _navigateToAddBeer

    private var currentSession = MutableLiveData<Session?>()

    val endSessionVisible = Transformations.map(currentSession) {
        null != it
    }

    var beers: LiveData<List<Beers>> = Transformations.switchMap(currentSession) {
        database.beersDao.getBySessionId(it?.id)
    }

    var liters: LiveData<Double> = Transformations.map(beers) { beers ->
        var sum = 0.000
        for (i in beers) {
            sum += i.count * i.type.value
        }
        sum / 1000
    }

    init {
        initializeSession()
    }

    fun onAddBeerClicked() {
        if (currentSession.value == null) {
            onStartSession()
        }

        _navigateToAddBeer.value = currentSession.value?.id
    }

    fun onEndSessionClicked() {
        onStopSession()
    }

    fun onNavigatedToAddBeer() {
        _navigateToAddBeer.value = null
    }

    private fun onStartSession() {
        viewModelScope.launch {
            // Create a new session, which captures the current time,
            // and insert it into the database.
            val session = Session()
            insert(session)
            currentSession.value = getSessionFromDatabase()
            _navigateToAddBeer.value = currentSession.value?.id
        }
    }

    private fun onStopSession() {
        viewModelScope.launch {
            var session = currentSession.value ?: return@launch
            session.isFinished = true
            update(session)
            currentSession.value = null
        }
    }

    private fun initializeSession() {
        viewModelScope.launch {
            currentSession.value = getSessionFromDatabase()
            Log.i("HomeViewModel", currentSession.value?.id.toString())
        }
    }

    private suspend fun getSessionFromDatabase(): Session? {
        return withContext(Dispatchers.IO) {
            var lastSession = database.sessionDao.getLastSession()
            if (lastSession?.isFinished == true) null else lastSession
        }
    }

    private suspend fun insert(session: Session) {
        withContext(Dispatchers.IO) {
            database.sessionDao.insert(session)
        }
    }

    private suspend fun update(session: Session) {
        withContext(Dispatchers.IO) {
            database.sessionDao.update(session)
        }
    }
}