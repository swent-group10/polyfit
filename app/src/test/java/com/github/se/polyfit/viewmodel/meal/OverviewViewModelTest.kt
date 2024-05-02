package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import android.util.Log
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class OverviewViewModelTest {

  private val mockMealDao: MealDao = mockk()
  private val mockSpoonacularApiCaller: SpoonacularApiCaller = mockk()

  private lateinit var overviewViewModel: OverviewViewModel

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    overviewViewModel = OverviewViewModel(mockMealDao, mockSpoonacularApiCaller)
  }

  @Test
  fun storeMeal_withNonNullBitmap_storesMeal() {
    val bitmap: Bitmap = mockk()
    val meal: Meal = mockk()
    every { mockSpoonacularApiCaller.getMealsFromImage(bitmap) } returns meal
    every { mockMealDao.insert(meal) } returns 1
    Assert.assertEquals(1.toLong(), overviewViewModel.storeMeal(bitmap))
  }

  @Test
  fun storeMeal_withNullBitmap_returnsNull() {
    val result = overviewViewModel.storeMeal(null)

    Assert.assertNull(result)
  }

  @Test
  fun deleteByDBId_deletesMeal() {
    val id = 1L
    every { mockMealDao.deleteByDatabaseID(id) } returns Unit

    // Call the method under test
    overviewViewModel.deleteByDBId(id)

    // Verify that the method was called with the correct parameters
    verify { mockMealDao.deleteByDatabaseID(id) }
  }

  @Test
  fun getMealsByCalorieRange_returnsCorrectMeals() {
    // Prepare the meals
    val meal1 =
        Meal(
            mealID = 1,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(Nutrient("calories", 100.0, MeasurementUnit.KCAL))),
            name = "Meal 1",
            occasion = MealOccasion.BREAKFAST)
    val meal2 =
        Meal(
            mealID = 2,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(Nutrient("calories", 200.0, MeasurementUnit.KCAL))),
            name = "Meal 2",
            occasion = MealOccasion.LUNCH)

    val meal3 =
        Meal(
            mealID = 3,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(Nutrient("calories", 300.0, MeasurementUnit.KCAL))),
            name = "Meal 3",
            occasion = MealOccasion.DINNER)
    val allMeals = listOf(meal1, meal2, meal3)

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals() } returns allMeals

    // Call the method under test
    val result = overviewViewModel.getMealsByCalorieRange(150.0, 250.0)

    // Verify the result
    Assert.assertEquals(1, result.size)
    Assert.assertEquals(meal2, result[0])
  }

  @Test
  fun getMealsByCalorieRange_emptyDatabase() {

    // Mock the getAllMeals function in the dao
    every { mockMealDao.getAllMeals() } returns listOf()

    // Call the method under test
    val result = overviewViewModel.getMealsByCalorieRange(150.0, 250.0)

    // Verify the result
    Assert.assertEquals(0, result.size)
  }
}
