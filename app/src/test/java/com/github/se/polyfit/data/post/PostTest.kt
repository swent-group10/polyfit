package com.github.se.polyfit.data.post

import android.location.Location
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.model.post.Post
import org.junit.Assert.assertEquals
import org.junit.Test

class PostTest {
  private val expectedNutrient = Nutrient("carb", 100.0, MeasurementUnit.G)
  private val meal: Meal =
      Meal(
          occasion = MealOccasion.OTHER,
          name = "Test Meal",
          mealID = 1,
          nutritionalInformation = NutritionalInformation(mutableListOf(expectedNutrient)))
  private val post = Post("userId", "description", Location("location"), meal)

  @Test
  fun `getCarbs returns correct nutrient`() {

    assertEquals(expectedNutrient, post.getCarbs())
  }

  @Test
  fun `getFat returns correct nutrient`() {
    val meal = Meal.default()
    val post = Post("userId", "description", Location("location"), meal)
    val expectedNutrient = Nutrient("fat", 100.0, MeasurementUnit.G)

    meal.nutritionalInformation.update(expectedNutrient)
    assertEquals(expectedNutrient, post.getFat())
  }

  @Test
  fun `getProtein returns correct nutrient`() {
    val meal = Meal.default()
    val post = Post("userId", "description", Location("location"), meal)
    val expectedNutrient = Nutrient("protein", 100.0, MeasurementUnit.G)
    meal.nutritionalInformation.update(expectedNutrient)

    assertEquals(expectedNutrient, post.getProtein())
  }

  @Test
  fun `getIngredientCalories returns correct list of pairs`() {
    val meal = Meal.default()
    val post = Post("userId", "description", Location("location"), meal)
    val nutrient = Nutrient("calories", 100.0, MeasurementUnit.KCAL)
    meal.ingredients.add(
        Ingredient(
            "ingredient1",
            100,
            100.3,
            MeasurementUnit.G,
            NutritionalInformation(mutableListOf(nutrient))))

    val expectedValue = listOf(Pair("ingredient1", nutrient))
    assertEquals(expectedValue, post.getIngredientCalories())
  }

  @Test
  fun `getIngredientWeight returns correct list of pairs`() {
    val meal = Meal.default()
    val post = Post("userId", "description", Location("location"), meal)
    val expectedList =
        listOf(Pair("ingredient1", Nutrient("totalWeight", 100.3, MeasurementUnit.G)))

    meal.ingredients.add(
        Ingredient(
            "ingredient1",
            100,
            100.3,
            MeasurementUnit.G,
            NutritionalInformation(
                mutableListOf(Nutrient("calories", 100.0, MeasurementUnit.KCAL)))))

    assertEquals(expectedList, post.getIngredientWeight())
  }
}
