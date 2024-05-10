package com.github.se.polyfit.viewmodel.dailyRecap

import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.meal.MealTagColor
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DailyRecapViewModelTest {

  private lateinit var viewModel: DailyRecapViewModel
  private val mockMealRepository = mockk<MealRepository>(relaxed = true)
  private var date = LocalDate.now()

  @OptIn(ExperimentalCoroutinesApi::class)
  @Before
  fun setup() {
    val meals = listOf(Meal.default(), testMeal)
    coEvery { mockMealRepository.getMealsOnDate(date) } returns meals
    Dispatchers.setMain(UnconfinedTestDispatcher())
    viewModel = DailyRecapViewModel(mockMealRepository)
  }

  private val testMeal =
      Meal(
          name = "Meal 1",
          ingredients =
              mutableListOf(
                  Ingredient(
                      name = "Tomato",
                      id = 1,
                      amount = 100.0,
                      unit = MeasurementUnit.UNIT,
                      nutritionalInformation =
                          NutritionalInformation(
                              mutableListOf(Nutrient("calories", 10.0, MeasurementUnit.KCAL))))),
          tags =
              mutableListOf(
                  MealTag("This is a long tag name", MealTagColor.BABYPINK),
                  MealTag("And yet another long", MealTagColor.LAVENDER),
                  MealTag("And yet another long long", MealTagColor.BRIGHTORANGE)),
          occasion = MealOccasion.BREAKFAST,
          createdAt = date)

  @Test
  fun initCallsSetDate() {
    val result = viewModel.meals.value
    assertEquals(1, result.size)
  }
}
