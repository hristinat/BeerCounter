package com.example.beercounter.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beercounter.addbeer.AddBeerViewModel
import com.example.beercounter.database.BeerCounterDatabase

class CalendarViewModelFactory(
    private val dataSource: BeerCounterDatabase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}