package com.github.se.polyfit.viewmodel.meal

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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.time.LocalDate
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
    val mealTask: Task<Meal?> = Mockito.mock(Task::class.java) as Task<Meal?>
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
    Mockito.`when`(mealRepo.getMeal(Mockito.anyString())).thenReturn(mealTask)

    viewModel = MealViewModel("user123", "firebase123", null, mealRepo)
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
    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)
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

    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)
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

    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)
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

    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)

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

    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)

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

    viewModel = MealViewModel("user123", initialMeal = initialMeal, mealRepo = mealRepo)

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
