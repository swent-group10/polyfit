package com.github.se.polyfit.model.ingredient

import android.util.Log
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import kotlin.test.assertFailsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class IngredientTest {
  private val ingredient =
      Ingredient("eggs", 1, 1.2, MeasurementUnit.G, NutritionalInformation(mutableListOf()))

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun serializeIngredient() {
    val map = Ingredient.serializeIngredient(ingredient)
    val expectedMap =
        mapOf(
            "name" to "eggs",
            "id" to 1,
            "amount" to 1.2,
            "unit" to MeasurementUnit.G.toString(),
            "nutritionalInformation" to
                NutritionalInformation.serialize(ingredient.nutritionalInformation))
    assert(map == expectedMap)
  }

  @Test
  fun deserializeIngredient() {
    Ingredient.serializeIngredient(ingredient).also { serializedIngredient ->
      val deserializedIngredient = Ingredient.deserializeIngredient(serializedIngredient)
      assert(ingredient == deserializedIngredient)
    }
  }
  // add more tests
  @Test
  fun testIngredientName() {
    assert(ingredient.name == "eggs")
  }

  @Test
  fun testSerializationDeserialization() {
    val serializedIngredient = Ingredient.serializeIngredient(ingredient)
    val deserializedIngredient = Ingredient.deserializeIngredient(serializedIngredient)
    assert(ingredient == deserializedIngredient)
    assert(ingredient.name == deserializedIngredient.name)
    assert(ingredient.nutritionalInformation == deserializedIngredient.nutritionalInformation)
  }

  @Test
  fun testSerializationDeserializationWithDefaultValues() {
    val ingredientWithDefaultValues =
        Ingredient("eggs", 1, 1.2, MeasurementUnit.G, NutritionalInformation(mutableListOf()))
    // Serialize the Ingredient object
    val serializedIngredient = Ingredient.serializeIngredient(ingredientWithDefaultValues)
    // Deserialize the serialized Ingredient object
    val deserializedIngredient = Ingredient.deserializeIngredient(serializedIngredient)
    assert(deserializedIngredient == ingredientWithDefaultValues)
  }

  @Test
  fun testDeserializationWithEmptyValues() {
    // Create a map with null values
    val map = mapOf("name" to "eggs", "nutritionalInformation" to "")

    // Deserialize the map
    assertFailsWith<Exception> { Ingredient.deserializeIngredient(map) }
  }

  @Test
  fun deserializeIngredient_withValidData_returnsIngredient() {
    val data =
        mapOf(
            "name" to "eggs",
            "id" to 1,
            "amount" to 10.2,
            "unit" to MeasurementUnit.G.toString(),
            "nutritionalInformation" to listOf<Map<String, Any>>())
    val result = Ingredient.deserializeIngredient(data)
    assertEquals("eggs", result.name)
    assertTrue(result.nutritionalInformation.nutrients.isEmpty())
    assertEquals(1, result.id)
  }

  @Test
  fun deserializeIngredient_withInvalidName_throwsException() {
    val data =
        mapOf("name" to 123, "id" to 1, "nutritionalInformation" to listOf<Map<String, Any>>())
    assertFailsWith<IllegalArgumentException> { Ingredient.deserializeIngredient(data) }
  }

  @Test
  fun deserializeIngredient_withInvalidId_throwsException() {
    val data =
        mapOf(
            "name" to "eggs",
            "id" to "invalid",
            "nutritionalInformation" to listOf<Map<String, Any>>())

    assertFailsWith<IllegalArgumentException> { Ingredient.deserializeIngredient(data) }
  }

  @Test
  fun deserializeIngredient_withInvalidNutritionalInformation_throwsException() {
    val data = mapOf("name" to "eggs", "id" to 1, "nutritionalInformation" to "invalid")

    assertFailsWith<IllegalArgumentException> { Ingredient.deserializeIngredient(data) }
  }
}
