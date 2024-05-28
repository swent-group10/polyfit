package com.github.se.polyfit.model.recipe

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import java.net.URL
import junit.framework.TestCase.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.junit.Before

class RecipeTest {

  private val validData =
      mapOf(
          "id" to 1L,
          "title" to "Recipe Title",
          "imageURL" to URL("http://example.com"),
          "usedIngredients" to 5L,
          "missingIngredients" to 2L,
          "likes" to 100L,
          "recipeInformation" to
              RecipeInformation(
                      vegetarian = true,
                      vegan = false,
                      glutenFree = true,
                      dairyFree = false,
                      ingredients = emptyList(),
                      instructions = listOf("instructions"))
                  .serialize())

  private val invalidData = mapOf<String, Any>("invalidKey" to "invalidValue")

  private val recipeInformation =
      RecipeInformation(
          vegetarian = true,
          vegan = false,
          glutenFree = true,
          dairyFree = false,
          ingredients = emptyList(),
          instructions = listOf("instructions"))

  private val recipe =
      Recipe(1L, "Recipe Title", URL("http://example.com"), 5L, 2L, 100L, recipeInformation)

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun deserializeReturnsCorrectRecipeForValidData() {
    val result = Recipe.deserialize(validData)

    assertEquals(1L, result.id)
    assertEquals("Recipe Title", result.title)
    assertEquals(URL("http://example.com"), result.imageUrl)
    assertEquals(5L, result.usedIngredients)
    assertEquals(2L, result.missingIngredients)
    assertEquals(100L, result.likes)
    assertEquals(listOf("instructions"), result.recipeInformation.instructions)
  }

  @Test
  fun deserializeThrowsIllegalArgumentExceptionForInvalidData() {
    assertFailsWith<IllegalArgumentException> { Recipe.deserialize(invalidData) }
  }

  @Test
  fun serializeReturnsCorrectDataForRecipe() {
    val result = Recipe.serialize(recipe)

    assertEquals(1L, result["id"])
    assertEquals("Recipe Title", result["title"])
    assertEquals(URL("http://example.com"), result["imageURL"])
    assertEquals(5L, result["usedIngredients"])
    assertEquals(2L, result["missingIngredients"])
    assertEquals(100L, result["likes"])
    assertEquals(
        listOf("instructions"), (result["recipeInformation"] as Map<String, Any>)["instructions"])
  }

  @Test
  fun serializeAndDeserializeDefaultMeal() {
    val recipe = Recipe.default()
    val serialized = Recipe.serialize(recipe)
    val deserialized = Recipe.deserialize(serialized)

    assertEquals(recipe, deserialized)
  }
}
