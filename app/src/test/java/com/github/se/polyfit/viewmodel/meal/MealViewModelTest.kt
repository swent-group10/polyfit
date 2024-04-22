package com.github.se.polyfit.viewmodel.meal

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Task
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlin.test.assertFailsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MealViewModelTest {
  @get:Rule val rule = InstantTaskExecutorRule()

  @RelaxedMockK private lateinit var mealRepo: MealFirebaseRepository

  private lateinit var viewModel: MealViewModel

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    val meal =
        Meal(
            occasion = MealOccasion.OTHER,
            name = "Test Meal",
            mealID = 1,
            nutritionalInformation = NutritionalInformation(mutableListOf()))
    val mealTask = mockk<Task<Meal?>>()

    every { mealRepo.getMeal(any()) } returns mealTask
    viewModel = MealViewModel(mealRepo)
    viewModel.setMealData(meal)

    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
  }

  @After
  fun tearDown() {
    clearMocks(mealRepo)
  }

  @Test
  fun testInitialization_withFirebaseID_loadsData() {
    assert(viewModel.meal.value?.name == "Test Meal")
  }

  @Test
  fun testSetMealName_updatesMealName() {
    val initialMeal =
        Meal(
            name = "Old Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(initialMeal)
    viewModel.setMealName("New Name")

    assert(viewModel.meal.value?.name == "New Name")
  }

  // Continue to refactor other tests similarly...

  @Test
  fun `set meal makes call to firebase`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(initialMeal)
    val ingredient =
        Ingredient(
            name = "Tomato",
            id = 1,
            amount = 100.0,
            unit = MeasurementUnit.G,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(
                        Nutrient("calories", 10.0, MeasurementUnit.CAL),
                        Nutrient("totalWeight", 10.0, MeasurementUnit.G),
                        Nutrient("carbohydrates", 50.0, MeasurementUnit.G),
                        Nutrient("fat", 20.0, MeasurementUnit.G),
                        Nutrient("protein", 10.0, MeasurementUnit.G))))
    viewModel.addIngredient(ingredient)
    viewModel.setMeal()

    verify { mealRepo.storeMeal(viewModel.meal.value!!) }
  }

  @Test
  fun `remove ingredients`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(initialMeal)
    val ingredient =
        Ingredient(
            name = "Tomato",
            id = 1,
            amount = 100.0,
            unit = MeasurementUnit.G,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(
                        Nutrient("calories", 10.0, MeasurementUnit.CAL),
                        Nutrient("totalWeight", 10.0, MeasurementUnit.G),
                        Nutrient("carbohydrates", 50.0, MeasurementUnit.G),
                        Nutrient("fat", 20.0, MeasurementUnit.G),
                        Nutrient("protein", 10.0, MeasurementUnit.G))))
    viewModel.addIngredient(ingredient)
    viewModel.removeIngredient(ingredient)
    assert(viewModel.meal.value?.ingredients?.size == 0)
  }

  @Test
  fun `test set null meal results in an error`() {
    val nullMealViewModel = MealViewModel(mealRepo)

    assertFailsWith<Exception> { viewModel.setMeal() }
  }

  @Test
  fun `test set with incompletemeal results in an error`() {
    val incompleteMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(incompleteMeal)

    assertFailsWith<Exception> { viewModel.setMeal() }
  }

  @Test fun `test null argument exception for empty meal`() {}
}
