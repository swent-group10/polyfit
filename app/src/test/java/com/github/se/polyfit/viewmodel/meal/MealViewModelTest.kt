package com.github.se.polyfit.viewmodel.meal

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.meal.MealTagColor
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Task
import java.time.LocalDate
import kotlin.test.assertFailsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MealViewModelTest {
  @Rule @JvmField val rule = InstantTaskExecutorRule()

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

  @Test
  fun `firebase fails when trying to store meal`() {
    //        coEvery { mealRepo.storeMeal(any()) } returns throw Exception("Error storing meal")
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation =
                NutritionalInformation(mutableListOf(Nutrient("fat", 10.2, MeasurementUnit.UNIT))),
            occasion = MealOccasion.BREAKFAST,
            firebaseId = "firebase123",
            ingredients = mutableListOf(Ingredient.default()))

    viewModel.setMealData(initialMeal)

    every { mealRepo.storeMeal(any()) } throws Exception("Error storing meal")

    assertFailsWith<Exception> { viewModel.setMeal() }
  }

  @Test
  fun `viewmodel with no meal creates default`() {
    viewModel = MealViewModel("user123", mealRepo = mealRepo)
    assert(viewModel.meal.value!! == Meal.default())
  }

  @Test
  fun `viewmodel with firebaseID loads data`() {
    Mockito.verify(mealRepo).getMeal("firebase123")
  }

  @Test
  fun `add tag updates meal tags`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST,
            tags = mutableListOf())
    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)
    val newTag = MealTag("Healthy", MealTagColor.CORAL)

    viewModel.addTag(newTag)

    assert(viewModel.meal.value?.tags?.contains(newTag) == true)
  }

  @Test
  fun `remove tag updates meal tags`() {
    val initialTag = MealTag("Healthy", MealTagColor.BABYPINK)
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST,
            tags = mutableListOf(initialTag))
    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)

    viewModel.removeTag(initialTag)

    assert(viewModel.meal.value?.tags?.contains(initialTag) == false)
  }

  @Test
  fun `set meal created at updates date`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)
    val newDate = LocalDate.now()

    viewModel.setMealCreatedAt(newDate)

    assert(viewModel.meal.value?.createdAt == newDate)
  }

  @Test
  fun `set meal occasion updates occasion`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)
    val newOccasion = MealOccasion.DINNER

    viewModel.setMealOccasion(newOccasion)

    assert(viewModel.meal.value?.occasion == newOccasion)
  }
}
