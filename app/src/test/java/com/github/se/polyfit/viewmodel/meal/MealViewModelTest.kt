package com.github.se.polyfit.viewmodel.meal

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MealViewModelTest {
  private val context = ApplicationProvider.getApplicationContext<Context>()

  @get:Rule val rule = InstantTaskExecutorRule()

  @Mock private lateinit var mealRepo: MealRepository

  private lateinit var viewModel: MealViewModel

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)
    val mealTask: Task<Meal?> = Mockito.mock(Task::class.java) as Task<Meal?>
    val mockMeal = Mockito.mock(Meal::class.java)
    val meal =
        Meal(
            occasion = MealOccasion.OTHER,
            name = "Test Meal",
            mealID = 1,
            nutritionalInformation = NutritionalInformation(mutableListOf()))
    Mockito.`when`(mealTask.addOnCompleteListener(Mockito.any())).thenAnswer { invocation ->
      val listener =
          invocation.getArgument(0, OnCompleteListener::class.java) as OnCompleteListener<Meal?>
      val meal =
          Meal(
              occasion = MealOccasion.OTHER,
              name = "Test Meal",
              mealID = 1,
              nutritionalInformation = NutritionalInformation(mutableListOf()))
      val mockResult = Mockito.mock(Task::class.java) as Task<Meal?>
      Mockito.`when`(mockResult.isSuccessful).thenReturn(true)
      Mockito.`when`(mockResult.result).thenReturn(meal)
      listener.onComplete(mockResult)
      return@thenAnswer mealTask
    }

    runBlocking { Mockito.`when`(mealRepo.getMeal(Mockito.anyString())).thenReturn(meal) }
    viewModel = MealViewModel("user123", context, "firebase123", null, mealRepo)
  }

  @After
  fun tearDown() {
    Mockito.reset(mealRepo)
  }

  @Test
  fun test1Initialization_withFirebaseID_loadsData() {
    // Check LiveData value

    assertEquals("Test Meal", viewModel.meal.value?.name)
  }

  @Test
  fun testSetMealName_updatesMealName() {
    val initialMeal =
        Meal(
            name = "Old Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel =
        MealViewModel("user123", initialMeal = initialMeal, context = context, mealRepo = mealRepo)
    viewModel.setMealName("New Name")

    assert(viewModel.meal.value?.name == "New Name")
  }

  @Test
  fun test2AddIngredient_updatesMeal() {
    val initialMeal =
        Meal(
            name = "Old Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel =
        MealViewModel("user123", initialMeal = initialMeal, context = context, mealRepo = mealRepo)
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
  fun `test3 set data meal`() {
    val initialMeal =
        Meal(
            name = "Old Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel =
        MealViewModel("user123", initialMeal = initialMeal, context = context, mealRepo = mealRepo)
    val meal =
        Meal(
            name = "New Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel.setMealData(meal)
    assert(viewModel.meal.value?.name == "New Name")
  }

  @Test
  fun `test4 remove ingredient`() {
    val ingredient =
        Ingredient(
            name = "Tomato",
            id = 1,
            amount = 100.0,
            unit = MeasurementUnit.G,
            nutritionalInformation =
                NutritionalInformation(
                    mutableListOf(Nutrient("calories", 0.0, MeasurementUnit.CAL))))
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = ingredient.nutritionalInformation,
            occasion = MealOccasion.BREAKFAST,
            ingredients = mutableListOf(ingredient))

    viewModel =
        MealViewModel("user123", initialMeal = initialMeal, context = context, mealRepo = mealRepo)

    viewModel.removeIngredient(ingredient)

    assert(viewModel.meal.value?.ingredients?.contains(ingredient) == false)
    assert(viewModel.meal.value?.nutritionalInformation?.getNutrient("calories")?.amount == 0.0)
  }

  @Test
  fun `test5 set meal with incomplete meal fails`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel =
        MealViewModel("user123", initialMeal = initialMeal, context = context, mealRepo = mealRepo)

    assertFailsWith<Exception> { viewModel.setMeal() }
  }

  @Test
  fun `test6 set meal makes call to firebase`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel =
        MealViewModel("user123", initialMeal = initialMeal, context = context, mealRepo = mealRepo)

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
    runBlocking { Mockito.verify(mealRepo).storeMeal(viewModel.meal.value!!) }
  }

  @Test
  fun `test7 viewmodel with no meal creates default`() {
    viewModel = MealViewModel("user123", context = context, mealRepo = mealRepo)
    assert(viewModel.meal.value!! == Meal.default())
  }

  @Test
  fun `test8 viewmodel with firebaseID loads data`() {
    runBlocking { Mockito.verify(mealRepo).getMeal("firebase123") }
  }
}
