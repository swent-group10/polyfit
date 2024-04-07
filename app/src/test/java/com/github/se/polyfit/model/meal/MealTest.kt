package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFailsWith

class MealTest {
    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @Test
    fun `Meal addIngredient should update meal`() {
        val meal =
            Meal(MealOccasion.DINNER, "eggs", 1, 102.2, NutritionalInformation(mutableListOf()))
        val newNutritionalInformation =
            NutritionalInformation(mutableListOf(Nutrient("calcium", 1.0, MeasurementUnit.G)))

        val ingredient = Ingredient("milk", 1, 102.0, MeasurementUnit.MG, newNutritionalInformation)
        meal.addIngredient(ingredient)
        // Assert that the meal has been updated after adding an ingredient
        assertEquals(1, meal.ingredients.size)

        // Assert that the meal's nutritional information has been updated
        assertEquals(1.0, meal.nutritionalInformation.nutrients[0].amount, 0.001)
    }

    @Test
    fun `Meal serialize should serialize meal correctly`() {
        val meal =
            Meal(MealOccasion.DINNER, "eggs", 1, 102.2, NutritionalInformation(mutableListOf()))
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
                "nutritionalInformation" to listOf<Map<String, Any>>()
            )
        // Make sure that an exception is thrown
        assertFailsWith<Exception> { Meal.deserialize(data) }
    }

    @Test
    fun `Meal deserialize should return meal if data is correct`() {
        val data =
            mapOf(
                "mealID" to 1,
                "occasion" to "DINNER",
                "name" to "eggs",
                "mealTemp" to 102.2,
                "nutritionalInformation" to NutritionalInformation(mutableListOf()).serialize()
            )
        val meal = Meal.deserialize(data)
        assertNotNull(meal)
        assertEquals(1, meal?.mealID)
        assertEquals(MealOccasion.DINNER, meal?.occasion)
        assertEquals("eggs", meal?.name)
        assertEquals(102.2, meal?.mealTemp)
    }

    @Test
    fun `testing deserialize with Firebase type`() {
        val data: Map<String, Any> =
            mapOf(
                "mealID" to 1,
                "occasion" to "DINNER",
                "name" to "eggs",
                "mealTemp" to 102.2,
                "nutritionalInformation" to listOf<Map<String, Any>>()
            )

        val meal = Meal.deserialize(data)
        val deserialized = Meal.deserialize(data)
        assertEquals(meal, deserialized)
    }
}
