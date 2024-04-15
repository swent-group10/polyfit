package com.github.se.polyfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

@Entity(tableName = "MealEntity")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val occasion: MealOccasion,
    val name: String,
    val mealID: Long,
    val mealTemp: Double,
    val nutritionalInformation: NutritionalInformation,
    val ingredients: MutableList<Ingredient>,
    val firebaseId: String
)
