package com.github.se.polyfit.meal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import io.mockk.mockk
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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

  @Before
  fun setup() {
    viewModel = MealViewModel(mealRepo, Meal.default(), "firebaseID")
  }

  @Test
  fun setMealName_updates_meal_name_and_completeness() = runBlocking {
    val mealName = "Test Meal"
    viewModel.setMealData(Meal.default())
    viewModel.setMealName(mealName)

    val updatedMeal = viewModel.meal.getOrAwait()

    assertEquals(mealName, updatedMeal.name)
    assertEquals(viewModel.isComplete.value, updatedMeal.isComplete())
  }

  @Test
  fun setMeal_incomplete_meal_throws_exception() {
    val incompleteMeal = Meal.default().apply { name = "" }
    val viewModelLocal = MealViewModel(mealRepo, incompleteMeal, "firebaseID")

    assertFailsWith<Exception> { viewModelLocal.setMeal() }
  }

  @Test
  fun removeIngredient_updates_meal_and_completeness() {
    val ingredient = Ingredient("Test Ingredient", 100, 100.0, MeasurementUnit.G)
    val baseMeal = Meal.default().apply { addIngredient(ingredient) }
    viewModel.setMealData(baseMeal.copy())

    viewModel.removeIngredient(ingredient)

    val updatedMeal = viewModel.meal.getOrAwait()

    assertFalse(updatedMeal.ingredients.contains(ingredient))
    assertEquals(viewModel.isComplete.value, updatedMeal.isComplete())
  }

  @Test
  fun addIngredient_updates_meal_and_completeness() {
    val ingredient = Ingredient("Test Ingredient", 100, 100.0, MeasurementUnit.G)
    val baseMeal =
        Meal.default()
            .apply { firebaseId = "firebaseID" }
            .apply { name = "Test Meal" }
            .apply {
              nutritionalInformation.update(Nutrient("calories", 100.0, MeasurementUnit.UG))
            }
    viewModel.setMealData(baseMeal.copy())

    viewModel.addIngredient(ingredient)

    val updatedMeal = viewModel.meal.getOrAwait()

    assertTrue(updatedMeal.ingredients.contains(ingredient))
    assertEquals(true, updatedMeal.isComplete())
  }

  @Test
  fun clearMeal_resets_to_default_values() {
    viewModel.clearMeal()

    val clearedMeal = viewModel.meal.getOrAwait()

    assertEquals(Meal.default().mealID, clearedMeal.mealID)
  }

  @Test
  fun setMealData_only_some_fields() {
    val viewModelLocal = MealViewModel(mealRepo, Meal.default(), "firebaseID")
    viewModelLocal.setMealData(mealOccasion = MealOccasion.SNACK, name = "New Name")

    var updatedMeal = viewModelLocal.meal.getOrAwait()

    assertEquals(MealOccasion.SNACK, updatedMeal.occasion)
    assertEquals("New Name", updatedMeal.name)

    val timeNow = LocalDate.now()
    viewModelLocal.setMealData(createdAt = timeNow)
    updatedMeal = viewModelLocal.meal.getOrAwait()
    assertEquals(timeNow, updatedMeal.createdAt)
  }

  @Test
  fun setNewMealObserver() {
    val newMeal = MutableLiveData(Meal.default())
    viewModel.setNewMealObserver(newMeal)

    val updatedMeal = viewModel.meal.getOrAwait()

    assertEquals(newMeal.value, updatedMeal)
  }
}

fun <T> LiveData<T>.getOrAwait(time: Long = 3, unit: TimeUnit = TimeUnit.SECONDS): T {
  var data: T? = null
  val latch = CountDownLatch(1)
  val observer =
      Observer<T> {
        data = it
        latch.countDown()
      }
  this.observeForever(observer)
  try {
    if (!latch.await(time, unit)) {
      throw TimeoutException("LiveData value was never set.")
    }
  } finally {
    this.removeObserver(observer)
  }
  return data!!
}
