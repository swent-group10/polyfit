package com.github.se.polyfit.model.meal

import java.util.Locale

/** Represents the occasion for a meal. At what time of the day the meal is eaten. */
enum class MealOccasion {
  BREAKFAST,
  LUNCH,
  DINNER,
  SNACK,
  OTHER;

  fun toLowerCaseString(): String {
    return this.name.first() + this.name.substring(1).lowercase(Locale.ROOT)
  }

  companion object {
    fun fromString(value: String): MealOccasion {
      return when (value) {
        "BREAKFAST" -> BREAKFAST
        "LUNCH" -> LUNCH
        "DINNER" -> DINNER
        "SNACK" -> SNACK
        "OTHER" -> OTHER
        else -> throw IllegalArgumentException("Invalid value for MealOccasion")
      }
    }
  }
}
