package com.example.beercounter.database

enum class BeerType(val value: Int) {
    THIRD(330),
    HALF(500),
    LITER(1000);

    companion object {
        fun fromValue(value: Int?): BeerType? {
            for (b in values()) {
                if (b.value == value) {
                    return b
                }
            }
            return null
        }
    }
}

//enum class BeerType {
//    THIRD,
//    HALF,
//    LITER
//}