package com.github.se.polyfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation

@Entity(tableName = "MealTable")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val occasion: MealOccasion,
    val name: String,
    val mealID: Long,
    val mealTemp: Double,
    val nutritionalInformation: NutritionalInformation,
    val ingredients: MutableList<Ingredient>,
    val firebaseId: String
) {

  fun toMeal(): Meal {
    return Meal(occasion, name, mealID, mealTemp, nutritionalInformation, ingredients, firebaseId)
  }

  companion object {
    fun toMealEntity(meal: Meal): MealEntity {
      return MealEntity(
          occasion = meal.occasion,
          name = meal.name,
          mealID = meal.mealID,
          mealTemp = meal.mealTemp,
          nutritionalInformation = meal.nutritionalInformation,
          ingredients = meal.ingredients,
          firebaseId = meal.firebaseId)
    }
  }
}
