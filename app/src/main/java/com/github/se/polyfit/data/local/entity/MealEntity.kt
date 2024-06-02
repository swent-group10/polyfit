package com.github.se.polyfit.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate

/** Represents a meal in the database. */
@Entity(tableName = "MealTable")
data class MealEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "userId", defaultValue = "testUserID") val userId: String,
    val occasion: MealOccasion,
    val name: String,
    val mealTemp: Double,
    val nutritionalInformation: NutritionalInformation,
    val ingredients: MutableList<Ingredient>,
    val createdAt: LocalDate,
    val tags: MutableList<MealTag>
) {

  fun toMeal(): Meal {
    return Meal(occasion, name, id, userId, mealTemp, ingredients, createdAt, tags)
  }

  companion object {
    fun toMealEntity(meal: Meal): MealEntity {
      return MealEntity(
          occasion = meal.occasion,
          name = meal.name,
          id = meal.id,
          userId = meal.userId,
          mealTemp = meal.mealTemp,
          nutritionalInformation = meal.nutritionalInformation,
          ingredients = meal.ingredients,
          createdAt = meal.createdAt,
          tags = meal.tags)
    }
  }
}
