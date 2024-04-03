package com.github.se.polyfit.model.ingredient

import android.util.Log
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class IngredientTest {
    private val ingredient = Ingredient("eggs", 1, NutritionalInformation(mutableListOf()))

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
                "nutritionalInformation" to
                        NutritionalInformation.serialize(ingredient.nutritionalInformation)
            )

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
//        assert(nutritionalInformation.nutrients.filter {  })
//        assert(nutritionalInformation.protein.unit == MeasurementUnit.G)
        // Add more assertions for other nutrients
    }

    @Test
    fun testSerializationDeserialization() {
        val serializedIngredient = Ingredient.serializeIngredient(ingredient)
        val deserializedIngredient = Ingredient.deserializeIngredient(serializedIngredient)!!
        assert(ingredient == deserializedIngredient)
        assert(ingredient.name == deserializedIngredient.name)
        assert(ingredient.nutritionalInformation == deserializedIngredient.nutritionalInformation)
    }

    @Test
    fun testSerializationDeserializationWithDefaultValues() {
        // Create an Ingredient object with null values
        val ingredientWithDefaultValues =
            Ingredient("eggs", 1, NutritionalInformation(mutableListOf()))

        // Serialize the Ingredient object
        val serializedIngredient = Ingredient.serializeIngredient(ingredientWithDefaultValues)

        // Deserialize the serialized Ingredient object
        val deserializedIngredient = Ingredient.deserializeIngredient(serializedIngredient)

        // Check if the deserialized Ingredient object is null
        assert(deserializedIngredient == ingredientWithDefaultValues)
    }

    @Test
    fun testDeserializationWithEmptyValues() {
        // Create a map with null values
        val map = mapOf("name" to "eggs", "nutritionalInformation" to "")

        // Deserialize the map
        assertFailsWith<Exception> {
            Ingredient.deserializeIngredient(map)
        }

    }
}
