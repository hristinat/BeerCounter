package com.example.beercounter

import androidx.room.TypeConverter
import com.example.beercounter.database.BeerType
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun beerTypeToInt(beerType: BeerType?): Int? {
        return beerType?.ordinal
    }

    @TypeConverter
    fun intToBeerType(value: Int?): BeerType? {
        return BeerType.values()[value!!]
    }
}