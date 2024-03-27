package com.github.se.polyfit.model.meal

import com.github.se.polyfit.model.ingredient.Ingredient
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class MealTest {
    companion object {
        private const val UID = "1"
        private const val NAME = "eggs"
        private const val CALORIES = 102.2
        private const val PROTEIN = 12301.3
        private const val CARBOHYDRATES = 1234.9
        private const val FAT = 12303.0
    }

    private lateinit var meal: Meal
    private lateinit var expectedMap: Pair<Map<String, Any>, List<Map<String, Any>>>

    @Before
    fun setUp() {
        val ingredientEgg = Ingredient(NAME, 1.2, CALORIES, PROTEIN, CARBOHYDRATES, FAT)
        meal = Meal(MealOccasion.DINNER, NAME, CALORIES, PROTEIN, CARBOHYDRATES, FAT)
        meal.updateUid(UID)
        meal.addIngredient(ingredientEgg)

        expectedMap = Pair(
            mapOf(
                "uid" to UID,
                "occasion" to MealOccasion.DINNER.name,
                "name" to NAME,
                "calories" to CALORIES,
                "protein" to PROTEIN,
                "carbohydrates" to CARBOHYDRATES,
                "fat" to FAT,
            ), listOf(Ingredient.serializeIngredient(ingredientEgg))
        )
    }

    @Test
    fun testMealSerialization() {
        val map = Meal.serializeMeal(meal)
        assertEquals(expectedMap, map)
    }

    @Test
    fun testMealDeserialization() {
        val mealDeserialized = Meal.deserializeMeal(expectedMap.first, expectedMap.second)
        assertEquals(meal, mealDeserialized)
    }

    @Test
    fun testMealUpdateAfterAddingIngredient() {
        val ingredient = Ingredient(NAME, 1.2, CALORIES, PROTEIN, CARBOHYDRATES, FAT)
        val meal = Meal(
            MealOccasion.DINNER,
            NAME,
            CALORIES,
            PROTEIN,
            CARBOHYDRATES,
            FAT,
            mutableListOf(ingredient)
        )
        val ingredient2 = Ingredient(NAME, 1.2, CALORIES, PROTEIN, CARBOHYDRATES, FAT)
        meal.addIngredient(ingredient2)
        assertEquals(204.4, meal.calories)
        assertEquals(24602.6, meal.protein)
        assertEquals(2469.8, meal.carbohydrates)
        assertEquals(24606.0, meal.fat)
    }
}