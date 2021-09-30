package com.example.beercounter.addbeer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beercounter.database.BeerCounterDatabase

class AddBeerViewModelFactory(
    private val sessionId: Long,
    private val dataSource: BeerCounterDatabase,
    private val app: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBeerViewModel::class.java)) {
            return AddBeerViewModel(sessionId, dataSource, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}