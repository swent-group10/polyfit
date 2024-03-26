package com.github.se.polyfit.model.ingredient

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class IngredientTest {
    @Test
    fun serializeIngredient() {
        val ingredient = Ingredient(
            "eggs",
            1.2,
            102.2,
            12301.3,
            1234.9,
            12303.0
        )

        val map = Ingredient.serializeIngredient(ingredient)
        val expectedMap = mapOf(
            "name" to "eggs",
            "servingSize" to 1.2,
            "calories" to 102.2,
            "protein" to 12301.3,
            "carbohydrates" to 1234.9,
            "fat" to 12303.0
        )

        assert(map == expectedMap)
    }

    @Test
    fun deserializeIngredient() {
        val map = mapOf(
            "name" to "eggs",
            "servingSize" to 1.2,
            "calories" to 102.2,
            "protein" to 12301.3,
            "carbohydrates" to 1234.9,
            "fat" to 12303.0
        )

        val ingredient = Ingredient.deserializeIngredient(map)
        val expectedIngredient = Ingredient(
            "eggs",
            1.2,
            102.2,
            12301.3,
            1234.9,
            12303.0
        )

        assert(ingredient == expectedIngredient)
    }
}