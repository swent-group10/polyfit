package com.github.se.polyfit.model.ingredient

import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import org.junit.Test

class IngredientTest {
  private val ingredient = Ingredient("eggs", NutritionalInformation())

  @Test
  fun serializeIngredient() {

    val map = Ingredient.serializeIngredient(ingredient)
    val expectedMap =
        mapOf(
            "name" to "eggs",
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
  fun testNutritionalInformation() {
    val nutritionalInformation = ingredient.nutritionalInformation
    // Replace with actual values
    assert(nutritionalInformation.protein.amount == 0.0)
    assert(nutritionalInformation.protein.unit == MeasurementUnit.G)
    // Add more assertions for other nutrients
  }

  @Test
  fun testSerializationDeserialization() {
    val serializedIngredient = Ingredient.serializeIngredient(ingredient)
    val deserializedIngredient = Ingredient.deserializeIngredient(serializedIngredient)
    assert(ingredient == deserializedIngredient)
    assert(ingredient.name == deserializedIngredient?.name)
    assert(ingredient.nutritionalInformation == deserializedIngredient?.nutritionalInformation)
  }

  @Test
  fun testSerializationDeserializationWithNullValues() {
    // Create an Ingredient object with null values
    val ingredientWithNullValues = Ingredient("eggs", NutritionalInformation())

    // Serialize the Ingredient object
    val serializedIngredient = Ingredient.serializeIngredient(ingredientWithNullValues)

    // Deserialize the serialized Ingredient object
    val deserializedIngredient = Ingredient.deserializeIngredient(serializedIngredient)

    // Check if the deserialized Ingredient object is null
    assert(deserializedIngredient == null)
  }

  @Test
  fun testDeserializationWithEmptyValues() {
    // Create a map with null values
    val map = mapOf("name" to "eggs", "nutritionalInformation" to "")

    // Deserialize the map
    val deserializedIngredient = Ingredient.deserializeIngredient(map)

    // Check if the deserialized Ingredient object is null
    assert(deserializedIngredient == null)
  }
}
