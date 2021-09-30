package com.example.beercounter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.beercounter.database.entities.Beers
import com.example.beercounter.database.entities.Session
import java.math.BigDecimal
import java.math.RoundingMode


@BindingAdapter("beerCount")
fun TextView.setBeerCount(beers: List<Beers>?) {
    beers?.let {
        text = if (beers.isNotEmpty()) {
            beers.size.toString()
        } else {
            "0"
        }
    }
}

@BindingAdapter("liters")
fun TextView.setLiters(liters: Double?) {
    text = liters?.toString() ?: "0"
}

@BindingAdapter("averageBeers")
fun TextView.setAverageBeers(beersSessions: Pair<List<Beers>?, List<Session>?>?) {
    text = if (beersSessions != null && beersSessions.first?.isNotEmpty()!!) {
        var average = beersSessions.first?.size?.toDouble()?.div(beersSessions.second?.size!!)
        if (average != null) {
            BigDecimal(average).setScale(2, RoundingMode.HALF_EVEN).toString()
        } else {
            "0"
        }
    } else {
        "0"
    }
}

@BindingAdapter("averageLiters")
fun TextView.setAverageLiters(beersSessions: Pair<List<Beers>?, List<Session>?>?) {
    if (beersSessions != null && beersSessions.first?.isNotEmpty()!!) {
        val beers = beersSessions.first
        var liters = 0.000
        if (beers != null) {
            for (i in beers) {
                liters += i.count * i.type.value
            }
        }
        liters /= 1000
        var averageLiters = liters / beersSessions.second?.size!!
        text = BigDecimal(averageLiters).setScale(2, RoundingMode.HALF_EVEN).toString()
    } else {
        text = "0"
    }
}