package com.github.se.polyfit.model.meal

enum class MealOccasion {
  BREAKFAST,
  LUNCH,
  DINNER,
  SNACK;

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
