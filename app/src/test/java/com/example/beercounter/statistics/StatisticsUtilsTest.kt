package com.example.beercounter.statistics

import com.example.beercounter.database.BeerType
import com.example.beercounter.database.entities.Beers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun testBeersToLitersOneSession() {
        val oneLiterBeer = Beers(type = BeerType.LITER, sessionId = 1)
        val oneThirdBeer = Beers(type = BeerType.THIRD, sessionId = 1)
        val oneHalfBeer = Beers(type = BeerType.HALF, sessionId = 1)

        assertThat(beersToLiters(listOf(oneLiterBeer, oneHalfBeer, oneThirdBeer)), `is`(1.830))
    }
}