package com.github.se.polyfit.model.recipe

import android.util.Log
import com.github.se.polyfit.data.api.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFailsWith

class RecipeInformationTest {

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @Test
    fun deserializeReturnsCorrectRecipeInformationValidData() {
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
                                        NutritionalInformation(mutableListOf()).serialize()
                            ),
                            mapOf(
                                "name" to "ingredient2",
                                "id" to 2.toLong(),
                                "amount" to 2.0,
                                "unit" to MeasurementUnit.TEASPOONS.name,
                                "nutritionalInformation" to
                                        NutritionalInformation(mutableListOf()).serialize()
                            )
                        ),
                "instructions" to "instructions"
            )

        val result = RecipeInformation.deserialize(data)

        assertEquals(true, result.vegetarian)
        assertEquals(false, result.vegan)
        assertEquals(true, result.glutenFree)
        assertEquals(false, result.dairyFree)
        assertEquals(2, result.ingredients.size)
        assertEquals("instructions", result.instructions)
    }

    @Test
    fun deserializeThrowsIllegalArgumentExceptionInvalidData() {
        val data = mapOf<String, Any>("invalidKey" to "invalidValue")

        assertFailsWith<IllegalArgumentException> { RecipeInformation.deserialize(data) }
    }

    @Test
    fun serializeReturnsCorrectDataRecipeInformation() {
        val ingredients =
            listOf(
                Ingredient(1, "beef tenderloin", "beef tenderloin", "beef_tenderloin.jpg"),

                )
        val recipeInformation =
            RecipeInformation(true, false, true, false, ingredients, listOf("instructions"))

        val result = RecipeInformation.serialize(recipeInformation)

        assertEquals(true, result["vegetarian"])
        assertEquals(false, result["vegan"])
        assertEquals(true, result["glutenFree"])
        assertEquals(false, result["dairyFree"])
        assertEquals(2, (result["listIngredients"] as List<*>).size)
        assertEquals("instructions", result["instructions"])
    }

    @Test
    fun serializeAndDeserializeDefault() {
        val recipeInformation = RecipeInformation.default()
        val result = RecipeInformation.serialize(recipeInformation)
        val deserialized = RecipeInformation.deserialize(result)
        assertEquals(recipeInformation, deserialized)
    }
}
