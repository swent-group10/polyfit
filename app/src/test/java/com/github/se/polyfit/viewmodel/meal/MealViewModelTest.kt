package com.github.se.polyfit.viewmodel.meal

import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MealViewModelTest {
  private val userID = "testUserID"
  private val firebaseID = "testFirebaseID"

  private lateinit var mealViewModel: MealViewModel
  private val mealFirebaseRepository: MealFirebaseRepository = mockk()

  @Before
  fun setup() {
    every { mealFirebaseRepository.getMeal(firebaseID) } returns mockk()
    every { mealFirebaseRepository.storeMeal(any()) } returns mockk()
  }

  @Test
  fun `viewmodel with empty paramters creates default`() {
    mealViewModel = MealViewModel(userID, mealRepo = mealFirebaseRepository)

    val meal = mealViewModel.meal.value!!

    assertEquals(meal.name, "")
    assertEquals(meal.occasion, MealOccasion.OTHER)
    assert(meal.nutritionalInformation.nutrients.isEmpty())
    assert(meal.ingredients.isEmpty())
    assertEquals(meal.firebaseId, "")
  }

  @Test
  fun `viewmodel with initial meal sets meal`() {
    val initialMeal =
        Meal(
            MealOccasion.BREAKFAST,
            "testMeal",
            1,
            20.0,
            NutritionalInformation(mutableListOf()),
            mutableListOf(),
            "testID")

    mealViewModel =
        MealViewModel(userID, initialMeal = initialMeal, mealRepo = mealFirebaseRepository)
    val meal = mealViewModel.meal.value!!

    assertEquals(initialMeal, meal)
  }

  // TODO: Setup the mock data for the getMeal method
  @Ignore
  @Test
  fun `viewmodel with firebaseID gets meal from repo`() {
    mealViewModel = MealViewModel(userID, firebaseID, mealRepo = mealFirebaseRepository)

    // verify that getMeal was called with the firebaseID
    verify { mealFirebaseRepository.getMeal(firebaseID) }
  }

  @Test
  fun `addIngredient adds ingredient to meal`() {
    val ingredient =
        Ingredient(
            "testIngredient", 1, 1.0, MeasurementUnit.G, NutritionalInformation(mutableListOf()))
    mealViewModel = MealViewModel(userID, mealRepo = mealFirebaseRepository)

    mealViewModel.addIngredient(ingredient)

    val meal = mealViewModel.meal.value!!
    assert(meal.ingredients.contains(ingredient))
  }

  @Test
  fun `removeIngredient removes ingredient from meal`() {
    val ingredient =
        Ingredient(
            "testIngredient", 1, 1.0, MeasurementUnit.G, NutritionalInformation(mutableListOf()))
    val initialMeal =
        Meal(
            MealOccasion.BREAKFAST,
            "testMeal",
            1,
            20.0,
            NutritionalInformation(mutableListOf()),
            mutableListOf(ingredient),
            "testID")
    mealViewModel =
        MealViewModel(userID, initialMeal = initialMeal, mealRepo = mealFirebaseRepository)

    mealViewModel.removeIngredient(ingredient)

    val meal = mealViewModel.meal.value!!
    assert(meal.ingredients.isEmpty())
  }

  @Test
  fun `setMeal throws exception if meal is not complete`() {
    mealViewModel = MealViewModel(userID, mealRepo = mealFirebaseRepository)

    assertFailsWith<Exception> { mealViewModel.setMeal() }
  }

  @Test
  fun `setMeal calls storeMeal on repo`() {
    val ingredient =
        Ingredient(
            "testIngredient", 1, 1.0, MeasurementUnit.G, NutritionalInformation(mutableListOf()))
    val meal =
        Meal(
            MealOccasion.BREAKFAST,
            "testMeal",
            1,
            20.0,
            NutritionalInformation(mutableListOf(Nutrient("testNutrient", 1.0, MeasurementUnit.G))),
            mutableListOf(ingredient),
            "testID")
    mealViewModel = MealViewModel(userID, initialMeal = meal, mealRepo = mealFirebaseRepository)

    mealViewModel.setMeal()

    verify { mealFirebaseRepository.storeMeal(meal) }
  }
}
