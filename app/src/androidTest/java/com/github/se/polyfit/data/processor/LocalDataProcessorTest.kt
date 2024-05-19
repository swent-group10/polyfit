package com.github.se.polyfit.data.processor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class LocalDataProcessorTest {
  private val mockMealDao: MealDao = mockk(relaxed = true)
  private val localDataProcessor = LocalDataProcessor(mockMealDao)
  private val mealList = createSampleMeals()

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  @Test
  fun getCaloriesForTodaysOccasions() {
    every { mockMealDao.getMealsCreatedOnOrAfterDate(LocalDate.now()) } returns mealList
    val result = localDataProcessor.getCaloriesPerMealOccasionToday()
    assertEquals(100.0, result[MealOccasion.BREAKFAST])
    assertEquals(200.0, result[MealOccasion.LUNCH])
    assertEquals(300.0, result[MealOccasion.DINNER])
    assertEquals(400.0, result[MealOccasion.SNACK])
  }

  @Test
  fun returnsZeroCaloriesForEachMealOccasionWhenNoMealsToday() {
    every { mockMealDao.getMealsCreatedOnOrAfterDate(LocalDate.now()) } returns emptyList()
    val result = localDataProcessor.getCaloriesPerMealOccasionToday()
    assertEquals(0.0, result[MealOccasion.BREAKFAST])
    assertEquals(0.0, result[MealOccasion.LUNCH])
    assertEquals(0.0, result[MealOccasion.DINNER])
    assertEquals(0.0, result[MealOccasion.SNACK])
  }

  @Test
  fun calculatesCaloriesCorrectlyWhenMultipleMealsForTheSameOccasionToday() {
    val meals =
        listOf(
            createCustomMeal(MealOccasion.BREAKFAST, 100.0),
            createCustomMeal(MealOccasion.BREAKFAST, 200.0),
            createCustomMeal(MealOccasion.LUNCH, 300.0),
            createCustomMeal(MealOccasion.LUNCH, 400.0))
    every { mockMealDao.getMealsCreatedOnOrAfterDate(LocalDate.now()) } returns meals
    val result = localDataProcessor.getCaloriesPerMealOccasionToday()
    assertEquals(300.0, result[MealOccasion.BREAKFAST])
    assertEquals(700.0, result[MealOccasion.LUNCH])
  }

  @Test
  fun returnsCorrectDailyCalorieSummariesForLastMonth() {
    val meals =
        listOf(
            createCustomMeal(date = LocalDate.now().minusMonths(1), calories = 100.0),
            createCustomMeal(date = LocalDate.now().minusMonths(1).plusDays(1), calories = 200.0),
            createCustomMeal(date = LocalDate.now().minusMonths(1).plusDays(2), calories = 300.0))
    every { mockMealDao.getMealsCreatedOnOrAfterDate(any()) } returns meals
    val result = localDataProcessor.getCaloriesLastMonth()
    assertEquals(3, result.size)
    assertEquals(100.0, result[0].totalCalories)
    assertEquals(200.0, result[1].totalCalories)
    assertEquals(300.0, result[2].totalCalories)
  }

  @Test
  fun returnsCorrectDailyCalorieSummariesForLastWeek() {
    val meals =
        listOf(
            createCustomMeal(date = LocalDate.now().minusWeeks(1), calories = 100.0),
            createCustomMeal(date = LocalDate.now().minusWeeks(1).plusDays(1), calories = 200.0),
            createCustomMeal(date = LocalDate.now().minusWeeks(1).plusDays(2), calories = 300.0))
    every { mockMealDao.getMealsCreatedOnOrAfterDate(any()) } returns meals
    val result = localDataProcessor.getCaloriesLastWeek()
    assertEquals(3, result.size)
    assertEquals(100.0, result[0].totalCalories)
    assertEquals(200.0, result[1].totalCalories)
    assertEquals(300.0, result[2].totalCalories)
  }

  @Test
  fun getCaloriesLiveData() = runTest {
    val meals = createSampleMeals()
    every { mockMealDao.getMealsCreatedOnDateLiveData(any()) } returns MutableLiveData(meals)

    val result = localDataProcessor.getCaloriesPerMealOccasionTodayLiveData()
    assertEquals(100.0, result.value?.find { it.first == MealOccasion.BREAKFAST }?.second)
    assertEquals(200.0, result.value?.find { it.first == MealOccasion.LUNCH }?.second)
    assertEquals(300.0, result.value?.find { it.first == MealOccasion.DINNER }?.second)
    assertEquals(400.0, result.value?.find { it.first == MealOccasion.SNACK }?.second)
  }

  private fun createSampleMeals() =
      listOf(
          createMeal("Oatmeal", MealOccasion.BREAKFAST, 100.0),
          createMeal("Rice", MealOccasion.LUNCH, 200.0),
          createMeal("Pasta", MealOccasion.DINNER, 300.0),
          createMeal("Chips", MealOccasion.SNACK, 400.0))

  private fun createMeal(name: String, occasion: MealOccasion, calories: Double) =
      Meal(
          occasion = occasion,
          name = name,
          mealTemp = 20.0,
          ingredients =
              mutableListOf(
                  Ingredient(
                      name,
                      100,
                      10.0,
                      MeasurementUnit.G,
                      NutritionalInformation(
                          mutableListOf(
                              Nutrient("calories", calories, MeasurementUnit.UG),
                              Nutrient("protein", 10.0, MeasurementUnit.G),
                              Nutrient("carbs", 20.0, MeasurementUnit.G),
                              Nutrient("fat", 5.0, MeasurementUnit.ML))))),
          id = "1",
          createdAt = LocalDate.now())

  private fun createCustomMeal(
      occasion: MealOccasion = MealOccasion.BREAKFAST,
      calories: Double,
      date: LocalDate = LocalDate.now()
  ) = createMeal("Generic Meal", occasion, calories).copy(createdAt = date)
}
