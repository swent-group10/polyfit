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
  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun `deserialize returns correct Recipe for valid data`() {
    val data =
        mapOf(
            "id" to 1L,
            "title" to "Recipe Title",
            "imageURL" to URL("http://example.com"),
            "usedIngredients" to 5L,
            "missingIngredients" to 2L,
            "likes" to 100L,
            "recipeInformation" to
                RecipeInformation(true, false, true, false, emptyList(), "instructions")
                    .serialize())

    val result = Recipe.deserialize(data)

    assertEquals(1L, result.id)
    assertEquals("Recipe Title", result.title)
    assertEquals(URL("http://example.com"), result.imageUrl)
    assertEquals(5L, result.usedIngredients)
    assertEquals(2L, result.missingIngredients)
    assertEquals(100L, result.likes)
    assertEquals("instructions", result.recipeInformation.instructions)
  }

  @Test
  fun `deserialize throws IllegalArgumentException for invalid data`() {
    val data = mapOf<String, Any>("invalidKey" to "invalidValue")

    assertFailsWith<IllegalArgumentException> { Recipe.deserialize(data) }
  }

  @Test
  fun `serialize returns correct data for Recipe`() {
    val recipeInformation = RecipeInformation(true, false, true, false, emptyList(), "instructions")
    val recipe =
        Recipe(1L, "Recipe Title", URL("http://example.com"), 5L, 2L, 100L, recipeInformation)

    val result = Recipe.serialize(recipe)

    assertEquals(1L, result["id"])
    assertEquals("Recipe Title", result["title"])
    assertEquals(URL("http://example.com"), result["imageURL"])
    assertEquals(5L, result["usedIngredients"])
    assertEquals(2L, result["missingIngredients"])
    assertEquals(100L, result["likes"])
    assertEquals("instructions", (result["recipeInformation"] as Map<String, Any>)["instructions"])
  }
}
