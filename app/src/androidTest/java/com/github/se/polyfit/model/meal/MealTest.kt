package com.github.se.polyfit.model.meal

import android.util.Log
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockkStatic
import java.time.LocalDate
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFailsWith
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before

class MealTest {
  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.e(any(), any(), any()) } returns 0
  }

  @Test
  fun MealAddIngredientUpdateMeal() {
    val meal = Meal(MealOccasion.DINNER, name = "eggs", mealTemp = 102.2)
    val newNutritionalInformation =
        NutritionalInformation(mutableListOf(Nutrient("calcium", 1.0, MeasurementUnit.G)))

    val ingredient = Ingredient("milk", 1, 102.0, MeasurementUnit.MG, newNutritionalInformation)
    meal.addIngredient(ingredient)
    assertEquals(1, meal.ingredients.size)
    assertEquals(1.0, meal.getNutrient("calcium")!!.amount, 0.001)
  }

  @Test
  fun mealSerializeSerializeMeal() {
    val meal =
        Meal(
            MealOccasion.DINNER,
            name = "eggs",
            mealTemp = 102.2,
            createdAt = LocalDate.parse("2021-01-01"),
            tags = mutableListOf(MealTag("name of tag", MealTagColor.BLUE)))
    val serializedMeal = Meal.serialize(meal)
    assertEquals(meal.id, serializedMeal["id"])
    assertEquals(MealOccasion.DINNER.name, serializedMeal["occasion"])
    assertEquals("eggs", serializedMeal["name"])
    assertEquals(102.2, serializedMeal["mealTemp"])
    assertEquals("2021-01-01", serializedMeal["createdAt"])

    assertEquals("name of tag", (serializedMeal["tags"] as List<Map<String, Any>>)[0]["tagName"])
    assertEquals("-3744015", (serializedMeal["tags"] as List<Map<String, Any>>)[0]["tagColor"])
  }

  @Test
  fun mealDeserializeReturnsNull() {
    val data =
        mapOf(
            "id" to UUID.randomUUID(),
            "occasion" to "DINNER",
            "name" to "eggs",
            "mealTemp" to "wrongValue",
            "createdAt" to "notARealDate")
    // Make sure that an exception is thrown
    assertFailsWith<Exception> { Meal.deserialize(data) }
  }

  @Test
  fun mealDeserializeShouldReturnsMeal() {
    val uuid = UUID.randomUUID().toString()
    val data =
        mapOf(
            "id" to uuid,
            "occasion" to "DINNER",
            "name" to "eggs",
            "mealTemp" to 102.2,
            "createdAt" to "2021-01-01",
            "tags" to mutableListOf(MealTag("name of tag", MealTagColor.BLUE).serialize()))
    val meal = Meal.deserialize(data)
    assertNotNull(meal)
    assertEquals(uuid, meal.id)
    assertEquals(MealOccasion.DINNER, meal.occasion)
    assertEquals("eggs", meal.name)
    assertEquals(102.2, meal.mealTemp, 0.001)
    assertEquals(LocalDate.parse("2021-01-01"), meal.createdAt)
    assertEquals("name of tag", meal.tags[0].tagName)
    assertEquals(MealTagColor.BLUE, meal.tags[0].tagColor)
  }

  @Test
  fun testingDeserializeWithFirebaseType() {
    val data: Map<String, Any> =
        mapOf(
            "id" to UUID.randomUUID().toString(),
            "occasion" to "DINNER",
            "name" to "eggs",
            "mealTemp" to 102.2,
            "createdAt" to "2021-01-01",
            "tags" to listOf<Map<String, Any>>())

    val meal = Meal.deserialize(data)
    val deserialized = Meal.deserialize(data)
    assertEquals(meal, deserialized)
  }

  @Test
  fun mealWithoutNameIsIncomplete() {
    val meal =
        Meal(
            MealOccasion.DINNER,
            name = "",
            mealTemp = 102.2,
            ingredients = mutableListOf(Ingredient("milk", 1, 102.0, MeasurementUnit.MG)))

    assertEquals(false, meal.isComplete())
  }

  @Test
  fun mealWithoutIngredientsIsIncomplete() {
    val meal =
        Meal(MealOccasion.DINNER, name = "eggs", mealTemp = 102.2, ingredients = mutableListOf())

    assertEquals(false, meal.isComplete())
  }

  @Test
  fun mealWithoutNutritionalInformationIsIncomplete() {
    val meal =
        Meal(
            MealOccasion.DINNER,
            name = "eggs",
            mealTemp = 102.2,
            ingredients = mutableListOf(Ingredient("milk", 1, 102.0, MeasurementUnit.MG)))

    assertEquals(false, meal.isComplete())
  }

  @Test
  fun mealWithAllMandatoryFieldsIsComplete() {
    val meal =
        Meal(
            MealOccasion.DINNER,
            name = "eggs",
            mealTemp = 102.2,
            ingredients =
                mutableListOf(
                    Ingredient(
                        "milk",
                        1,
                        102.0,
                        MeasurementUnit.MG,
                        NutritionalInformation(
                            mutableListOf(Nutrient("calcium", 1.0, MeasurementUnit.G))))))

    assertEquals(true, meal.isComplete())
  }

  @Test
  fun MealCalculateTotalCaloriesShouldReturnTotalCalories() {
    val meal =
        Meal(
            MealOccasion.DINNER,
            name = "eggs",
            mealTemp = 102.2,
            ingredients =
                mutableListOf(
                    Ingredient(
                        "milk",
                        1,
                        102.0,
                        MeasurementUnit.MG,
                        NutritionalInformation(
                            mutableListOf(
                                Nutrient("calories", 100.0, MeasurementUnit.CAL),
                                Nutrient("calcium", 1.0, MeasurementUnit.G))))))

    assertEquals(100.0, meal.calculateTotalCalories(), 0.001)
  }

  @Test
  fun MealCalculateTotalNutrientShouldReturnTotalNutrient() {
    val meal =
        Meal(
            MealOccasion.DINNER,
            name = "eggs",
            mealTemp = 102.2,
            ingredients =
                mutableListOf(
                    Ingredient(
                        "milk",
                        1,
                        102.0,
                        MeasurementUnit.MG,
                        NutritionalInformation(
                            mutableListOf(
                                Nutrient("calcium", 1.0, MeasurementUnit.G),
                                Nutrient("calories", 100.0, MeasurementUnit.CAL))))))

    assertEquals(1.0, meal.calculateTotalNutrient("calcium"), 0.001)
  }
}
