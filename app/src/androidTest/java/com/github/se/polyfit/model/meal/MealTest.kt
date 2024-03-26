package com.github.se.polyfit.model.meal

import com.github.se.polyfit.model.ingredient.Ingredient
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MealTest {
    private val ingredientEgg = Ingredient("eggs", 1.2, 102.2, 12301.3, 1234.9, 12303.0)

    private val meal = Meal(
        "1",
        MealOccasion.DINNER,
        "eggs",
        102.2,
        12301.3,
        1234.9,
        12303.0,
        listOf(ingredientEgg)
    )
    private val expectedMap = mapOf(
        "uid" to "1",
        "occasion" to MealOccasion.DINNER.name,
        "name" to "eggs",
        "calories" to 102.2,
        "protein" to 12301.3,
        "carbohydrates" to 1234.9,
        "fat" to 12303.0,
        "ingredients" to listOf(ingredientEgg).map { Ingredient.serializeIngredient(it) }
    )

    @Test
    fun testSerializeMeal() {
        val map = Meal.serializeMeal(meal)

        assert(map == expectedMap)
    }

    @Test
    fun testDeserializeMeal() {
        val mealDesr = Meal.deserializeMeal(expectedMap)

        assert(meal == mealDesr)
    }

    @Test
    fun checkMealCorrectlyUpdated() {
        val ingredient = Ingredient("eggs", 1.2, 102.2, 12301.3, 1234.9, 12303.0)
        val meal = Meal(
            "1",
            MealOccasion.DINNER,
            "eggs",
            102.2,
            12301.3,
            1234.9,
            12303.0,
            listOf(ingredient)
        )
        val ingredient2 = Ingredient("eggs", 1.2, 102.2, 12301.3, 1234.9, 12303.0)
        meal.addIngredient(ingredient2)
        assert(meal.getCalories() == 204.4)
        assert(meal.getProtein() == 24602.6)
        assert(meal.getCarbohydrates() == 2469.8)
        assert(meal.getFat() == 24606.0)
    }
}