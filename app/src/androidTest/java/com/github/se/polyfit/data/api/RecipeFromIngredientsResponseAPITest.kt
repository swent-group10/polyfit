package com.github.se.polyfit.data.api

import java.net.URL
import kotlin.test.Test
import org.json.JSONArray
import org.junit.Assert.assertEquals

class RecipeFromIngredientsResponseAPITest {

  @Test
  fun fromJsonObjectReturnsSuccessResponseWhenJSONIsValid() {
    val jsonArray =
        JSONArray(
            "[{\"id\":1,\"title\":\"Apple Pie\",\"image\":\"http://example.com/apple_pie.png\",\"likes\":\"100\",\"usedIngredientCount\":\"5\",\"missedIngredientCount\":\"0\"}]")

    val response = RecipeFromIngredientsResponseAPI.fromJsonObject(jsonArray)

    assertEquals(APIResponse.SUCCESS, response.status)
    assertEquals(1, response.recipes.size)
    assertEquals(1, response.recipes[0].id)
    assertEquals("Apple Pie", response.recipes[0].title)
    assertEquals(URL("http://example.com/apple_pie.png"), response.recipes[0].imageUrl)
    assertEquals(100, response.recipes[0].likes)
    assertEquals(5, response.recipes[0].usedIngredients)
    assertEquals(0, response.recipes[0].missingIngredients)
  }

  @Test
  fun fromJsonObjectReturnsFailureResponseWhenJSONIsInvalid() {
    val jsonArray = JSONArray("[{\"invalid\":\"data\"}]")

    val response = RecipeFromIngredientsResponseAPI.fromJsonObject(jsonArray)

    assertEquals(APIResponse.FAILURE, response.status)
    assertEquals(0, response.recipes.size)
  }

  @Test
  fun fromJsonObjectReturnsFailureResponseWhenJSONEmpty() {
    val jsonArray = JSONArray("[]")

    val response = RecipeFromIngredientsResponseAPI.fromJsonObject(jsonArray)

    assertEquals(APIResponse.SUCCESS, response.status)
    assertEquals(0, response.recipes.size)
  }
}
