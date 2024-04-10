package com.github.se.polyfit.viewmodel.meal

import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.data.api.APIResponse
import com.github.se.polyfit.data.api.RecipeNutritionResponseAPI
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MealViewModelTest {

  @MockK private var mealRepository: MealRepository = mockk(relaxed = true)

  private lateinit var viewModel: MealViewModel

  private var spoonacularApiCaller: SpoonacularApiCaller = mockk(relaxed = true)

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private val meal =
      Meal(
          MealOccasion.DINNER,
          "name",
          123,
          20.3,
          NutritionalInformation(mutableListOf()),
          mutableListOf(Ingredient("ingredient", 20, 30.2, MeasurementUnit.G)))

  @Before
  fun setUp() {
    viewModel = MealViewModel(mealRepository, spoonacularApiCaller)
  }

  @Test
  fun testGetAllMeals_Success() = runTest {
    val expectedMeals = listOf(meal)
    every { mealRepository.getAllMeals() } returns
        Tasks.forResult(expectedMeals) // Adapt this mock to correctly reflect your repository API

    val observer = mockk<Observer<List<Meal?>>>(relaxed = true)
    viewModel.getAllMeals().observeForever(observer)

    verify { observer.onChanged(expectedMeals) }
  }

  @Test
  fun testGetAllMeals_Faillure() = runTest {
    val expectedMeals = listOf(meal)
    every { mealRepository.getAllMeals() } returns
        Tasks.forException(
            Exception(
                "An error occurred")) // Adapt this mock to correctly reflect your repository API

    val observer = mockk<Observer<List<Meal?>>>(relaxed = true)
    viewModel.getAllMeals().observeForever(observer)

    verify { observer.onChanged(listOf(Meal.default())) }
  }

  @Test
  fun getMealsFromImage() {
    val inputStream =
        InstrumentationRegistry.getInstrumentation().context.assets.open("cheesecake.jpg")
    val bitmap = BitmapFactory.decodeStream(inputStream)
    every { spoonacularApiCaller.imageAnalysis(any<File>()) } returns
        mockk {
          every { status } returns APIResponse.SUCCESS
          every { category } returns "dessert"
          every { recipes } returns listOf(102, 103, 104)
        }

    every { spoonacularApiCaller.getMealNutrition(any()) } returns
        RecipeNutritionResponseAPI(
            APIResponse.SUCCESS,
            listOf(Nutrient("calories", 100.0, MeasurementUnit.CAL)),
            listOf(),
        )
    val docId: DocumentReference = mockk(relaxed = true)
    every { mealRepository.storeMeal(any()) } returns Tasks.forResult(docId)
    every { docId.id } returns "123"

    val observer = mockk<Observer<Meal>>(relaxed = true)
    viewModel.getMealsFromImage(bitmap).observeForever(observer)

    // Actions and verification
    coEvery { observer.onChanged(match { it.name == "dessert" && it.firebaseId == "123" }) } answers
        {
          nothing
        }

    viewModel.getMealsFromImage(bitmap) // This should trigger LiveData update

    // Verify that the meal in the live data has the expected properties
    coVerify { observer.onChanged(match { it.name == "dessert" && it.firebaseId == "123" }) }

    // Cleanup: Remove observer
    viewModel.getMealsFromImage(bitmap).removeObserver(observer)
  }

  @Test
  fun setMeal() {
    every { mealRepository.storeMeal(any()) } returns Tasks.forResult(mockk())
    val observer = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.setMeal(meal).observeForever(observer)
    verify { observer.onChanged(true) }
  }

  @Test
  fun setMealFaillure() {
    every { mealRepository.storeMeal(any()) } returns
        Tasks.forException(Exception("An error occurred"))
    val observer = mockk<Observer<Boolean>>(relaxed = true)
    viewModel.setMeal(meal).observeForever(observer)
    verify { observer.onChanged(false) }
  }

  @Test
  fun getAllIngredients() {
    val expectedMeal =
        listOf(
            Meal(
                MealOccasion.DINNER,
                "name",
                123,
                20.3,
                NutritionalInformation(mutableListOf()),
                mutableListOf(Ingredient("ingredient", 20, 30.2, MeasurementUnit.G))),
            Meal(
                MealOccasion.LUNCH,
                "candy",
                123,
                20.3,
                NutritionalInformation(mutableListOf()),
                mutableListOf(Ingredient("ingredient", 20, 30.2, MeasurementUnit.G))))

    val docId: DocumentReference = mockk(relaxed = true)
    every { mealRepository.storeMeal(expectedMeal.first()) } returns Tasks.forResult(docId)
    every { mealRepository.storeMeal(expectedMeal[1]) } returns Tasks.forResult(docId)

    for (meal in expectedMeal) {
      viewModel.setMeal(meal)
    }

    val observer = mockk<Observer<List<Ingredient>>>(relaxed = true)
    viewModel.getAllIngredients().observeForever(observer)

    verify { observer.onChanged(expectedMeal.flatMap { it.ingredients }) }
  }
}
