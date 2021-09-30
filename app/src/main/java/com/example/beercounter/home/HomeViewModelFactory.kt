package com.example.beercounter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.dao.BeersDao
import com.example.beercounter.database.dao.SessionDao

class HomeViewModelFactory (
    private val dataSource: BeerCounterDatabase) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}