package com.github.se.polyfit.ui.utils

import java.time.LocalDate

data class GraphData(val kCal: Double, val date: LocalDate, val weight: Double) {
  fun doesMatchSearchQuery(query: String): Boolean {
    val matchingCombinations =
        listOf(
            "$kCal",
            "${date.dayOfMonth}",
            "${date.monthValue}",
            "${date.year}",
            "$kCal ${date.dayOfMonth}",
            "$kCal ${date.monthValue}",
            "$kCal ${date.year}",
            "$kCal ${date.dayOfMonth} ${date.monthValue}",
            "$kCal ${date.dayOfMonth} ${date.monthValue} ${date.year}",
            "${date.dayOfMonth} ${date.monthValue}",
            "${date.dayOfMonth} ${date.monthValue} ${date.year}",
            "$weight",
            "$weight ${date.dayOfMonth} ${date.monthValue}",
            "$weight ${date.dayOfMonth} ${date.monthValue} ${date.year}",
        )

    return matchingCombinations.any { it.contains(query, ignoreCase = true) }
  }
}
