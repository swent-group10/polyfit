package com.github.se.polyfit.model.recipe

import android.util.Log
import com.github.se.polyfit.data.api.Spoonacular.Ingredient
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.junit.Before

class RecipeInformationTest {
  private val recipeInformation =
      RecipeInformation(
          true,
          false,
          true,
          false,
          listOf(
              Ingredient(1, "beef tenderloin", "beef tenderloin", "beef_tenderloin.jpg"),
              Ingredient(2, "onion", "onion", "onion.jpg")),
          listOf("instructions"))

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun deserializeReturnsCorrectRecipeInformationValidData() {
    val data = recipeInformation.serialize()

    val result = RecipeInformation.deserialize(data)

    assertEquals(true, result.vegetarian)
    assertEquals(false, result.vegan)
    assertEquals(true, result.glutenFree)
    assertEquals(false, result.dairyFree)
    assertEquals(2, result.ingredients.size)
    assertEquals(listOf("instructions"), result.instructions)
  }

  @Test
  fun deserializeThrowsIllegalArgumentExceptionInvalidData() {
    val data = mapOf<String, Any>("invalidKey" to "invalidValue")

    assertFailsWith<IllegalArgumentException> { RecipeInformation.deserialize(data) }
  }

  @Test
  fun serializeReturnsCorrectDataRecipeInformation() {
    val result = RecipeInformation.serialize(recipeInformation)

    assertEquals(true, result["vegetarian"])
    assertEquals(false, result["vegan"])
    assertEquals(true, result["glutenFree"])
    assertEquals(false, result["dairyFree"])
    assertEquals(2, (result["listIngredients"] as List<*>).size)
    assertEquals(listOf("instructions"), result["instructions"])
  }

  @Test
  fun serializeAndDeserializeDefault() {
    val recipeInformation = RecipeInformation.default()
    val result = RecipeInformation.serialize(recipeInformation)
    val deserialized = RecipeInformation.deserialize(result)
    assertEquals(recipeInformation, deserialized)
  }
}
