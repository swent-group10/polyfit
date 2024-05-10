package com.github.se.polyfit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate

@Entity(tableName = "MealTable")
data class MealEntity(
    @PrimaryKey val id: String,
    val occasion: MealOccasion,
    val name: String,
    val mealTemp: Double,
    val nutritionalInformation: NutritionalInformation,
    val ingredients: MutableList<Ingredient>,
    val createdAt: LocalDate,
    val tags: MutableList<MealTag>
) {

  fun toMeal(): Meal {
    return Meal(occasion, name, id, mealTemp, ingredients, createdAt, tags)
  }

  companion object {
    fun toMealEntity(meal: Meal): MealEntity {
      return MealEntity(
          occasion = meal.occasion,
          name = meal.name,
          id = meal.id,
          mealTemp = meal.mealTemp,
          nutritionalInformation = meal.nutritionalInformation,
          ingredients = meal.ingredients,
          createdAt = meal.createdAt,
          tags = meal.tags)
    }
  }
}
