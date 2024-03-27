package com.github.se.polyfit

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealsRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test

class MealsFirebaseConnectionTest {
  @Test
  fun addMealActualFirebase() {
    val database = MealsRepository()
    val meal = Meal(MealOccasion.DINNER, "eggs", 102.2, 12301.3, 1234.9, 12303.0)
    val newIngredient = Ingredient("eggs", 1.2, 102.2, 12301.3, 1234.9, 12303.0)
    meal.addIngredient(newIngredient)
    val result = database.addMeal("1", meal)

    result.addOnSuccessListener { assert(true) }.addOnFailureListener { assert(false) }
  }

  @Test
  fun getAllMealsActualFirebase() {
    val database = MealsRepository()
    val result = database.getAllMeals("1")

    runBlocking { result.await() }

    result.result?.forEach { Log.d("Meal", it.toString()) }
  }
}
