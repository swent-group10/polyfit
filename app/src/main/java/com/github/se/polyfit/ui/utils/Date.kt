package com.github.se.polyfit.ui.utils

// Temporary, might be changed when Actual Data is used
data class Date(val day: Int, val month: Int, val year: Int) : Comparable<Date> {
  override fun compareTo(other: Date): Int {
    if (this.year != other.year) {
      return this.year - other.year
    }
    if (this.month != other.month) {
      return this.month - other.month
    }
    return this.day - other.day
  }

  override fun toString(): String {
    return "${this.day}.${this.month}.${this.year}"
  }

  companion object {
    // Function to generate a list of dates between two dates
    fun generateDateRange(startDate: Date, endDate: Date): List<Date> {
      val dates = mutableListOf<Date>()
      var currentDate = startDate
      while (currentDate <= endDate) {
        dates.add(currentDate)
        currentDate = currentDate.nextDay()
      }
      return dates
    }

    // Function to sort a list of dates
    fun sortDates(dates: List<Date>): List<Date> {
      return dates.sorted()
    }
  }

  // Function to get the next day, considering leap years and month lengths
  private fun nextDay(): Date {
    val monthDays =
        if (month == 2) {
          if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) 29 else 28
        } else if (month in setOf(4, 6, 9, 11)) {
          30
        } else {
          31
        }

    val nextDay = day + 1
    val nextMonth = if (nextDay > monthDays) month + 1 else month
    val nextYear = if (nextMonth > 12) year + 1 else year
    val newDay = if (nextDay > monthDays) 1 else nextDay
    val newMonth = if (nextMonth > 12) 1 else nextMonth

    return Date(newDay, newMonth, nextYear)
  }
}
