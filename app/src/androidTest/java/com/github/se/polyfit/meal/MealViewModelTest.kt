package com.github.se.polyfit.meal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import io.mockk.mockk
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MealViewModelTest {

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var viewModel: MealViewModel
  private val mealRepo = mockk<MealRepository>(relaxed = true)
  private val context = mockk<android.content.Context>(relaxed = true)
  private val mealObserver = mockk<Observer<Meal>>(relaxed = true)
  private val completionObserver = mockk<Observer<Boolean>>(relaxed = true)

  @Before
  fun setup() {
    viewModel = MealViewModel("userId", context, "firebaseID", Meal.default(), mealRepo)
    viewModel.meal.observeForever(mealObserver)
    viewModel.isComplete.observeForever(completionObserver)
  }

  @Test
  fun setMealName_updates_meal_name_and_completeness() = runBlocking {
    val mealName = "Test Meal"
    viewModel.setMealData(Meal.default())
    viewModel.setMealName(mealName)

    val latch = CountDownLatch(1)
    viewModel.meal.observeForever {
      if (it.name == mealName) {
        latch.countDown()
      }
    }

    latch.await(3, TimeUnit.SECONDS) // wait for 2 seconds

    assertEquals("Meal name should be updated", mealName, viewModel.meal.value?.name)
    assertEquals(
        "Completeness should be updated",
        viewModel.isComplete.value,
        viewModel.meal.value?.isComplete())
  }

  @Test
  fun setMeal_incomplete_meal_throws_exception() {
    val incompleteMeal = Meal.default().apply { name = "" }
    val viewModelLocal = MealViewModel("userId", context, "firebaseID", incompleteMeal, mealRepo)

    assertFailsWith<Exception> { viewModelLocal.setMeal() }
  }

  @Test
  fun removeIngredient_updates_meal_and_completeness() {
    val ingredient = Ingredient("Test Ingredient", 100, 100.0, MeasurementUnit.G)
    val baseMeal = Meal.default().apply { addIngredient(ingredient) }
    viewModel.setMealData(baseMeal.copy())

    viewModel.removeIngredient(ingredient)

    val latch = CountDownLatch(1)
    viewModel.meal.observeForever {
      if (!it.ingredients.contains(ingredient)) {
        latch.countDown()
      }
    }

    latch.await(2, TimeUnit.SECONDS) // wait for 2 seconds

    assertFalse(
        "Ingredient should be removed", viewModel.meal.value!!.ingredients.contains(ingredient))
    assertEquals(
        "Completeness should be updated",
        viewModel.isComplete.value,
        viewModel.meal.value?.isComplete())
  }

  @Test
  fun setMeal_throws_exception_when_meal_is_null() {
    val viewModelLocal = MealViewModel("userId", context, "firebaseID", null, mealRepo)

    assertFailsWith<Exception> { viewModelLocal.setMeal() }
  }

  @Test
  fun setMeal_throws_exception_when_meal_is_incomplete() {
    val viewModelLocal = MealViewModel("userId", context, "firebaseID", Meal.default(), mealRepo)

    assertFailsWith<Exception> { viewModelLocal.setMeal() }
  }

  @Test
  fun addIngredient_updates_meal_and_completeness() {
    val ingredient = Ingredient("Test Ingredient", 100, 100.0, MeasurementUnit.G)
    val baseMeal = Meal.default()

    assertFalse(baseMeal.isComplete())
    viewModel.setMealData(baseMeal.copy())

    viewModel.addIngredient(ingredient)

    val latch = CountDownLatch(1)
    viewModel.meal.observeForever {
      if (it.ingredients.contains(ingredient)) {
        latch.countDown()
      }
    }

    latch.await(2, TimeUnit.SECONDS) // wait for 2 seconds

    if (viewModel.meal.value!!.ingredients.isNotEmpty()) {
      assertEquals("Ingredient should be added", viewModel.meal.value!!.ingredients[0], ingredient)
    }
    assertEquals(
        "Completeness should be updated",
        viewModel.isComplete.value,
        viewModel.meal.value?.isComplete())
  }

  @Test
  fun clearMeal() {
    val viewModelLocal = MealViewModel("userId", context, "firebaseID", Meal.default(), mealRepo)
    viewModelLocal.clearMeal()

    val latch = CountDownLatch(1)

    viewModelLocal.meal.observeForever {
      if (it.firebaseId == "") {
        latch.countDown()
      }
    }

    latch.await(2, TimeUnit.SECONDS) // wait for 2 seconds

    assertEquals(Meal.default().mealID, viewModelLocal.meal.value?.mealID)
  }
}
