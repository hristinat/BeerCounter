package com.example.beercounter.addbeer

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.BeerType.Companion.fromValue
import com.example.beercounter.database.entities.Beers
import com.example.beercounter.receiver.AlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBeerViewModel(
    private val sessionId: Long,
    private val database: BeerCounterDatabase,
    private val app: Application
) : AndroidViewModel(app) {
    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val REQUEST_CODE = 0
    private val second: Long = 1_000L

    private val notifyPendingIntent: PendingIntent
    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notifyIntent = Intent(app, AlarmReceiver::class.java)
    private var _alarmOn = MutableLiveData<Boolean>()

    init {
        _alarmOn.value = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_NO_CREATE
        ) != null

        notifyPendingIntent = PendingIntent.getBroadcast(
            getApplication(),
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun startTimer() {
        _alarmOn.value?.let {
            if (!it) {
                _alarmOn.value = true
                val time = second * 10
                val triggerTime = SystemClock.elapsedRealtime() + time

                val notificationManager = ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager
                notificationManager.cancelAll()

                AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    notifyPendingIntent
                )
            }
        }
    }

    fun onButtonClicked(beerTypeInt: Int) {
        _navigateToHome.value = true

        val beerType = fromValue(beerTypeInt)
        viewModelScope.launch {
            val beer = beerType?.let { Beers(type = it, sessionId = sessionId) }
            beer?.let { insert(it) }
            Log.i("AddBeer", "launch")
        }

        Log.i("AddBeer", "en  d")

        startTimer()
        _alarmOn.value = false
    }

    fun onNavigatedToHome() {
        _navigateToHome.value = false
    }

    private suspend fun insert(beer: Beers) {
        withContext(Dispatchers.IO) {
            database.beersDao.insert(beer)
            Log.i("AddBeer", "insert")
        }
    }
}