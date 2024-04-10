package com.github.se.polyfit.viewmodel.meal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealViewModelTest {
  @MockK private var mealRepository: MealRepository = mockk(relaxed = true)

  private lateinit var viewModel: MealViewModel

  private var spoonacularApiCaller: SpoonacularApiCaller = mockk(relaxed = true)

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private val meal =
      Meal(
          MealOccasion.DINNER,
          "name",
          123,
          20.3,
          NutritionalInformation(mutableListOf()),
          mutableListOf(Ingredient("ingredient", 20, 30.2, MeasurementUnit.G)))

  // TODO: Add more tests
}
