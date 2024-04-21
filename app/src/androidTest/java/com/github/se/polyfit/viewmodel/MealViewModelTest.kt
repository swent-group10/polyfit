package com.github.se.polyfit.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
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
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertFailsWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
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

    val meal =
        Meal(
            occasion = MealOccasion.OTHER,
            name = "Test Meal",
            mealID = 1,
            nutritionalInformation = NutritionalInformation(mutableListOf()))

    every { mealDao.getMealByFirebaseID(any()) } returns meal
    coEvery { mealRepo.getMeal(any()) } returns meal

    viewModel = MealViewModel(mealRepo)
    viewModel.setMealData(meal)
    viewModel.setFirebaseID("firebase123")
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
    viewModel.setMealName("New Name")
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

    assert(viewModel.meal.value?.ingredients?.contains(ingredient) == false)
    assert(viewModel.meal.value?.nutritionalInformation?.getNutrient("calories")?.amount == 0.0)
  }

  @Test
  fun testSetMealWithIncompleteMealFails() {
    val incompleteMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(incompleteMeal)
    assertFailsWith<Exception> { viewModel.setMeal() }
  }

  @Test
  fun testSetMealMakesCallToRepository() {
    val completeMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(
                        Nutrient(
                            nutrientType = "calories",
                            amount = 100.0,
                            unit = MeasurementUnit.CAL))),
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "Tomato",
                        id = 1,
                        amount = 100.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 0.0, MeasurementUnit.CAL))))),
            occasion = MealOccasion.BREAKFAST)
    runBlockingTest { coEvery { mealRepo.storeMeal(any()) } returns null }
    viewModel.setMealData(completeMeal)
    viewModel.setMeal()

    runBlockingTest { coEvery { mealRepo.storeMeal(viewModel.meal.value!!) } }
  }

  @Test
  fun testClearMeal() {
    viewModel.clearMeal()

    assert(viewModel.meal.value == Meal.default())
  }

  @Test
  fun testSetMealData() {
    val meal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(
                        Nutrient(
                            nutrientType = "calories",
                            amount = 100.0,
                            unit = MeasurementUnit.CAL))),
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "Tomato",
                        id = 1,
                        amount = 100.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 0.0, MeasurementUnit.CAL))))),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(meal)
    assert(viewModel.meal.value == meal)

    viewModel.setMealData()
    assert(viewModel.meal.value == meal)
  }

  @Test
  fun testSetNewMealObserver() {
    val newMeal =
        Meal(
            name = "New Meal",
            mealID = 123,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(
                        Nutrient(
                            nutrientType = "calories",
                            amount = 100.0,
                            unit = MeasurementUnit.CAL))),
            ingredients =
                mutableListOf(
                    Ingredient(
                        name = "Tomato",
                        id = 1,
                        amount = 100.0,
                        unit = MeasurementUnit.G,
                        nutritionalInformation =
                            NutritionalInformation(
                                mutableListOf(Nutrient("calories", 0.0, MeasurementUnit.CAL))),
                    )),
            occasion = MealOccasion.BREAKFAST)

    viewModel.setNewMealObserver(MutableLiveData(newMeal))

    assert(viewModel.meal.value == newMeal)
  }
}
