package com.github.se.polyfit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertFailsWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MealViewModelTest {
  @get:Rule val rule = InstantTaskExecutorRule()

  private val mealRepo: MealRepository = mockk()
  private lateinit var viewModel: MealViewModel

  @Before
  fun setUp() {
    Dispatchers.setMain(TestCoroutineDispatcher())

    val mealDao = mockk<MealDao>()
    val mealFirebaseRepository = mockk<MealFirebaseRepository>()

    val meal = Meal(occasion = MealOccasion.OTHER, name = "Test Meal", mealID = 1)

    every { mealDao.getMealByFirebaseID(any()) } returns meal
    coEvery { mealRepo.getMealByFirebaseID(any()) } returns meal

    viewModel = MealViewModel(mealRepo)
    viewModel.setMealData(meal)
    viewModel.updateMealData(firebaseID = "firebase123")
  }

  @After
  fun tearDown() {
    // Clear all MockK confirmations and stubbings
    io.mockk.clearMocks(mealRepo)
  }

  @Test
  fun testInitialization_withFirebaseID_loadsData() {
    assert(viewModel.meal.value?.name == "Test Meal")
  }

  @Test
  fun testSetMealName_updatesMealName() {
    viewModel.updateMealData(name = "New Name")
    assert(viewModel.meal.value?.name == "New Name")
  }

  @Test
  fun testAddIngredient_updatesMeal() {
    val ingredient =
        Ingredient(
            name = "Tomato",
            id = 1,
            amount = 100.0,
            unit = MeasurementUnit.G,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(
                        Nutrient("calories", 0.0, MeasurementUnit.CAL),
                        Nutrient("totalWeight", 0.0, MeasurementUnit.G),
                        Nutrient("carbohydrates", 0.0, MeasurementUnit.G),
                        Nutrient("fat", 0.0, MeasurementUnit.G),
                        Nutrient("protein", 0.0, MeasurementUnit.G))))

    viewModel.addIngredient(ingredient)
    assert(viewModel.meal.value?.ingredients?.contains(ingredient) == true)
  }

  @Test
  fun testRemoveIngredient_updatesMeal() {
    val ingredient =
        Ingredient(
            name = "Tomato",
            id = 1,
            amount = 100.0,
            unit = MeasurementUnit.G,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(Nutrient("calories", 0.0, MeasurementUnit.CAL))))

    viewModel.addIngredient(ingredient)
    viewModel.removeIngredient(ingredient)

    assert(!viewModel.meal.value.ingredients.contains(ingredient))
    assert(viewModel.meal.value.nutritionalInformation.getNutrient("calories") == null)
  }

  @Test
  fun testSetMealWithIncompleteMealFails() {
    val incompleteMeal = Meal(name = "Meal Name", mealID = 123, occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(incompleteMeal)
    assertFailsWith<Exception> { viewModel.setMeal() }
  }

  @Test
  fun testSetMealMakesCallToRepository() {
    val completeMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "Tomato",
                        id = 1,
                        amount = 100.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 100.0, MeasurementUnit.CAL))))),
            occasion = MealOccasion.BREAKFAST)
    runBlockingTest { coEvery { mealRepo.storeMeal(any()) } returns null }
    viewModel.setMealData(completeMeal)
    viewModel.setMeal()

    runBlockingTest { coEvery { mealRepo.storeMeal(viewModel.meal.value!!) } }
  }

  @Test
  fun testSetMealData() {
    val meal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "Tomato",
                        id = 1,
                        amount = 100.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 100.0, MeasurementUnit.CAL))))),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(meal)
    assert(viewModel.meal.value == meal)

    viewModel.updateMealData()
    assert(viewModel.meal.value == meal)
  }

  @Test
  fun setMealData_withValidMealId_setsMeal() = runTest {
    val mealId = 1L
    val expectedMeal =
        Meal(
            mealID = mealId,
            name = "Test Meal",
            occasion = MealOccasion.OTHER,
            mealTemp = 0.0,
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "Tomato",
                        id = 1,
                        amount = 100.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(
                                    Nutrient("calories", 0.0, MeasurementUnit.CAL),
                                    Nutrient("totalWeight", 0.0, MeasurementUnit.G),
                                )),
                    )))

    coEvery { mealRepo.getMealById(any()) } returns expectedMeal

    viewModel.setMealData(mealId)

    coVerify { mealRepo.getMealById(mealId) }
    assertThat(viewModel.meal.value, `is`(expectedMeal))
  }

  @Test
  fun setMealData_withInvalidMealId_throwsException() = runTest {
    val mealId = 1L

    coEvery { mealRepo.getMealById(any()) } returns null

    viewModel.setMealData(mealId)

    coVerify { mealRepo.getMealById(any()) }
    assertThat(viewModel.meal.value, `is`(Meal.default()))
  }
}
