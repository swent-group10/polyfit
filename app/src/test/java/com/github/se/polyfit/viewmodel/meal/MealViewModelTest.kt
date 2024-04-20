package com.github.se.polyfit.viewmodel.meal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlin.test.assertFailsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MealViewModelTest {
  @get:Rule val rule = InstantTaskExecutorRule()

  @Mock private lateinit var mealRepo: MealFirebaseRepository

  private lateinit var viewModel: MealViewModel

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)
    val meal =
        Meal(
            occasion = MealOccasion.OTHER,
            name = "Test Meal",
            mealID = 1,
            nutritionalInformation = NutritionalInformation(mutableListOf()))
    val mealTask: Task<Meal?> = Mockito.mock(Task::class.java) as Task<Meal?>
    Mockito.`when`(mealTask.addOnCompleteListener(Mockito.any())).thenAnswer { invocation ->
      val listener =
          invocation.getArgument(0, OnCompleteListener::class.java) as OnCompleteListener<Meal?>

      val mockResult = Mockito.mock(Task::class.java) as Task<Meal?>
      Mockito.`when`(mockResult.isSuccessful).thenReturn(true)
      Mockito.`when`(mockResult.result).thenReturn(meal)
      listener.onComplete(mockResult)
      return@thenAnswer mealTask
    }
    Mockito.`when`(mealRepo.getMeal(Mockito.anyString())).thenReturn(mealTask)

    viewModel = MealViewModel(User(id = "user123"), mealRepo)
    viewModel.setMealData(meal)
    viewModel.setFirebaseID("firebase123")
  }

  @After
  fun tearDown() {
    Mockito.reset(mealRepo)
  }

  @Test
  fun testInitialization_withFirebaseID_loadsData() {
    // Check LiveData value
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
    viewModel = MealViewModel(User(id = "user123"), mealRepo)
    viewModel.setMealData(initialMeal)
    viewModel.setMealName("New Name")

    assert(viewModel.meal.value?.name == "New Name")
  }

  @Test
  fun testAddIngredient_updatesMeal() {
    val initialMeal =
        Meal(
            name = "Old Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel = MealViewModel(User(id = "user123"), mealRepo)
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
                        Nutrient("calories", 0.0, MeasurementUnit.CAL),
                        Nutrient("totalWeight", 0.0, MeasurementUnit.G),
                        Nutrient("carbohydrates", 0.0, MeasurementUnit.G),
                        Nutrient("fat", 0.0, MeasurementUnit.G),
                        Nutrient("protein", 0.0, MeasurementUnit.G))))

    viewModel.addIngredient(ingredient)

    assert(viewModel.meal.value?.ingredients?.contains(ingredient) == true)
  }

  @Test
  fun `set data meal`() {
    val initialMeal =
        Meal(
            name = "Old Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel = MealViewModel(User(id = "user123"), mealRepo)
    viewModel.setMealData(initialMeal)
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
  fun `remove ingredient`() {
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

    viewModel = MealViewModel(User(id = "user123"), mealRepo)
    viewModel.setMealData(initialMeal)
    viewModel.removeIngredient(ingredient)

    assert(viewModel.meal.value?.ingredients?.contains(ingredient) == false)
    assert(viewModel.meal.value?.nutritionalInformation?.getNutrient("calories")?.amount == 0.0)
  }

  @Test
  fun `set meal with incomplete meal fails`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel = MealViewModel(User(id = "user123"), mealRepo)
    viewModel.setMealData(initialMeal)
    assertFailsWith<Exception> { viewModel.setMeal() }
  }

  @Test
  fun `set meal makes call to firebase`() {
    val initialMeal =
        Meal(
            name = "Meal Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)

    viewModel = MealViewModel(User(id = "user123"), mealRepo)
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

    Mockito.verify(mealRepo).storeMeal(viewModel.meal.value!!)
  }

  @Test
  fun `viewmodel with no meal creates default`() {
    viewModel = MealViewModel(User(id = "user123"), mealRepo)
    assert(viewModel.meal.value!! == Meal.default())
  }
}
