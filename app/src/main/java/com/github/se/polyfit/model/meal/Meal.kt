package com.github.se.polyfit.model.meal

// modeled after the log meal api
data class Meal(
    val uid : String,
    val occasion: MealOccasion,
    val name : String,
    val calories: Double,
    val protein: Double,
    val carbohydrates: Double,
    val fat: Double
)
