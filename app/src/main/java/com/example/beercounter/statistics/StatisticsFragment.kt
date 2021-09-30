package com.example.beercounter.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStatisticsBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val database = BeerCounterDatabase.getInstance(application)
        val viewModelFactory = StatisticsViewModelFactory(database)
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(StatisticsViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}