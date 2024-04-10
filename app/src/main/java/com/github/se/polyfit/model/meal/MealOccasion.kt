package com.github.se.polyfit.model.meal

import java.util.Locale

enum class MealOccasion {
  BREAKFAST,
  LUNCH,
  DINNER,
  SNACK;

  fun toCapitalizedString(): String {
    return this.name.first() + this.name.substring(1).lowercase(Locale.ROOT)
  }

  companion object {
    fun fromString(value: String): MealOccasion {
      return when (value) {
        "BREAKFAST" -> BREAKFAST
        "LUNCH" -> LUNCH
        "DINNER" -> DINNER
        "SNACK" -> SNACK
        else -> throw IllegalArgumentException("Invalid value for MealOccasion")
      }
    }
  }
}
