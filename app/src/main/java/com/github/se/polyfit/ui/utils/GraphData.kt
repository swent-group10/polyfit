package com.github.se.polyfit.ui.utils

data class GraphData(val kCal : Double, val day : Int, val month : String, val weight : Double) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations =
            listOf(
                "$kCal",
                "$day",
                month,
                "$weight",
                "$day$month",
                "$day $month",
                "$kCal $day",
                "$kCal $month",
                "$kCal $day $month",
                "$weight $day",
                "$weight $month",
                "$weight $day $month")

        return matchingCombinations.any { it.contains(query, ignoreCase = true) }
    }
}

