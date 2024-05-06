package com.github.se.polyfit.model.recipe

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.junit.Before

class RecipeInformationTest {

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun `deserialize returns correct RecipeInformation for valid data`() {
    val data =
        mapOf(
            "vegetarian" to true,
            "vegan" to false,
            "glutenFree" to true,
            "dairyFree" to false,
            "listIngredients" to
                listOf<Map<String, Any>>(
                    mapOf(
                        "name" to "ingredient1",
                        "id" to 1.toLong(),
                        "amount" to 1.0,
                        "unit" to MeasurementUnit.TEASPOONS.name,
                        "nutritionalInformation" to
                            NutritionalInformation(mutableListOf()).serialize()),
                    mapOf(
                        "name" to "ingredient2",
                        "id" to 2.toLong(),
                        "amount" to 2.0,
                        "unit" to MeasurementUnit.TEASPOONS.name,
                        "nutritionalInformation" to
                            NutritionalInformation(mutableListOf()).serialize())),
            "instructions" to "instructions")

    val result = RecipeInformation.deserialize(data)

    assertEquals(true, result.vegetarian)
    assertEquals(false, result.vegan)
    assertEquals(true, result.glutenFree)
    assertEquals(false, result.dairyFree)
    assertEquals(2, result.listIngredients.size)
    assertEquals("instructions", result.instructions)
  }

  @Test
  fun `deserialize throws IllegalArgumentException for invalid data`() {
    val data = mapOf<String, Any>("invalidKey" to "invalidValue")

    assertFailsWith<IllegalArgumentException> { RecipeInformation.deserialize(data) }
  }

  @Test
  fun `serialize returns correct data for RecipeInformation`() {
    val ingredients =
        listOf(
            Ingredient("ingredient1", 1, 1.0, MeasurementUnit.TEASPOONS),
            Ingredient("ingredient2", 2, 2.0, MeasurementUnit.TEASPOONS))
    val recipeInformation = RecipeInformation(true, false, true, false, ingredients, "instructions")

    val result = RecipeInformation.serialize(recipeInformation)

    assertEquals(true, result["vegetarian"])
    assertEquals(false, result["vegan"])
    assertEquals(true, result["glutenFree"])
    assertEquals(false, result["dairyFree"])
    assertEquals(2, (result["listIngredients"] as List<*>).size)
    assertEquals("instructions", result["instructions"])
  }

  @Test
  fun `serialize and deserialize default`() {
    val recipeInformation = RecipeInformation.default()
    val result = RecipeInformation.serialize(recipeInformation)
    val deserialized = RecipeInformation.deserialize(result)
    assertEquals(recipeInformation, deserialized)
  }
}
