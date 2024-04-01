package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.Before

class MealTest {
  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun `Meal addIngredient should update meal`() {
    val meal = Meal(MealOccasion.DINNER, "eggs", 1, 102.2, NutritionalInformation())
    val ingredient = Ingredient("milk", 1, NutritionalInformation())
    meal.addIngredient(ingredient)
    // Assert that the meal has been updated after adding an ingredient
  }

  @Test
  fun `Meal serialize should serialize meal correctly`() {
    val meal = Meal(MealOccasion.DINNER, "eggs", 1, 102.2, NutritionalInformation())
    val serializedMeal = Meal.serialize(meal)
    assertEquals(1, serializedMeal["mealID"])
    assertEquals(MealOccasion.DINNER.name, serializedMeal["occasion"])
    assertEquals("eggs", serializedMeal["name"])
    assertEquals(102.2, serializedMeal["mealTemp"])
  }

  @Test
  fun `Meal deserialize should return null if data is incorrect`() {
    val data =
        mapOf(
            "mealID" to 1,
            "occasion" to "DINNER",
            "name" to "eggs",
            "mealTemp" to "wrongValue",
            "nutritionalInformation" to mapOf<String, Any>())
    val meal = Meal.deserialize(data)
    assertNull(meal)
  }

  @Test
  fun `Meal deserialize should return meal if data is correct`() {
    val data =
        mapOf(
            "mealID" to 1,
            "occasion" to "DINNER",
            "name" to "eggs",
            "mealTemp" to 102.2,
            "nutritionalInformation" to NutritionalInformation().serialize())
    val meal = Meal.deserialize(data)
    assertNotNull(meal)
    assertEquals(1, meal?.mealID)
    assertEquals(MealOccasion.DINNER, meal?.occasion)
    assertEquals("eggs", meal?.name)
    assertEquals(102.2, meal?.mealTemp)
  }
}
