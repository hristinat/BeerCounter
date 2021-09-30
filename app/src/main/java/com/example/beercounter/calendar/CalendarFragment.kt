package com.example.beercounter.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.EventDay
import com.example.beercounter.R
import com.example.beercounter.database.BeerCounterDatabase
import com.example.beercounter.database.entities.Beers
import com.example.beercounter.databinding.FragmentCalendarBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class CalendarFragment : Fragment() {

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentCalendarBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val database = BeerCounterDatabase.getInstance(application)
        val viewModelFactory = CalendarViewModelFactory(database)
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(CalendarViewModel::class.java)
        val events: MutableList<EventDay> = ArrayList()

        viewModel.mediatorLiveData.observe(viewLifecycleOwner, { data ->
            if (data != null) {
                val sessions = data.second
                val beers = data.first
                if (sessions?.isNotEmpty() == true) {
                    for (item in sessions) {
                        val calendar = Calendar.getInstance()
                        calendar.time = item.date
                        var eventDay = EventDay(calendar, R.drawable.ic_baseline_sports_bar_24)
                        events.add(eventDay)
                    }

                    binding.calendar.setEvents(events)

                    binding.calendar.setOnDayClickListener { eventDay ->
                        if (eventDay.imageDrawable != null) {
                            val sessions =
                                sessions.filter { it.date.date == eventDay.calendar.time.date && it.date.year == eventDay.calendar.time.year
                                        && it.date.month == eventDay.calendar.time.month}
                            var dayBeers = mutableListOf<Beers>()
                            for (session in sessions) {
                                dayBeers.addAll(beers?.filter { it.sessionId == session.id } ?: emptyList())
                            }
                            var liters = 0.000
                            if (dayBeers != null) {
                                for (i in dayBeers) {
                                    liters += i.count * i.type.value
                                }
                            }
                            liters /= 1000
                            MaterialAlertDialogBuilder(requireContext())
                                .setMessage("Beers: ${dayBeers.size}\nLiters: $liters")
                                .show()
                        }
                    }
                }
            }
        })

//        binding.calendar.setOnDayClickListener {
//            if (it.imageDrawable != null) {
//                MaterialAlertDialogBuilder(requireContext())
////                    .setTitle("test")
//                    .setMessage("fsgg")
//                    .show()
//            }
//        }
        return binding.root
    }
}