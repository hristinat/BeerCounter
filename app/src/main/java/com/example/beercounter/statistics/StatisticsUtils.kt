package com.example.beercounter.statistics

import com.example.beercounter.database.entities.Beers

fun beersToLiters(beers: List<Beers>): Double {
    var sum = 0.000
    for (i in beers) {
        sum += i.count * i.type.value
    }
    return sum / 1000
}