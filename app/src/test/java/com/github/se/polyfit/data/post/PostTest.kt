package com.github.se.polyfit.model.post

import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class PostTest {

  @Test
  fun `getCarbs returns correct nutrient when present`() {
    val expectedNutrient = Nutrient("carb", 100.0, MeasurementUnit.G)
    val meal = Meal.default()
    meal.nutritionalInformation.update(expectedNutrient)
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(expectedNutrient, post.getCarbs())
  }

  @Test
  fun `getCarbs returns null when nutrient not present`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(null, post.getCarbs())
  }

  @Test
  fun `serialize returns correct map`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())
    val expectedMap =
        mapOf(
            "userId" to "userId",
            "description" to "description",
            "location" to Location(0.0, 0.0, 10.0, "EPFL"),
            "meal" to meal.serialize(),
            "createdAt" to LocalDate.now())

    assertEquals(expectedMap, post.serialize())
  }

  @Test
  fun `getIngredientCalories returns correct list when nutrients present`() {
    val nutrient = Nutrient("calories", 100.0, MeasurementUnit.KCAL)
    val meal = Meal.default()
    val ingredient =
        Ingredient(
            "meal",
            100,
            100.0,
            MeasurementUnit.KCAL,
            NutritionalInformation(mutableListOf(nutrient)))
    meal.ingredients.add(ingredient)
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())
    val expectedList = listOf(Pair("meal", nutrient))

    assertEquals(expectedList, post.getIngredientCalories())
  }

  @Test
  fun `getIngredientCalories returns empty list when nutrients not present`() {
    val meal = Meal.default()
    val post =
        Post("userId", "description", Location(0.0, 0.0, 10.0, "EPFL"), meal, LocalDate.now())

    assertEquals(emptyList<Pair<String, Nutrient>>(), post.getIngredientCalories())
  }
}
