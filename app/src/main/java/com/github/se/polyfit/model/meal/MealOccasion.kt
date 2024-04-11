package com.github.se.polyfit.model.meal

enum class MealOccasion {
  BREAKFAST,
  LUNCH,
  DINNER,
  SNACK,
  OTHER;

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
