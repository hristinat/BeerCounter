package com.example.beercounter.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beercounter.database.BeerCounterDatabase

class StatisticsViewModelFactory(
    private val dataSource: BeerCounterDatabase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}