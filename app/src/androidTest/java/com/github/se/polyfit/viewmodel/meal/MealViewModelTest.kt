package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import androidx.lifecycle.Observer
import com.github.se.polyfit.data.api.ImageAnalysisResponseAPI
import com.github.se.polyfit.data.api.RecipeNutritionResponseAPI
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.meal.Meal
import com.google.android.gms.tasks.Tasks
import io.mockk.Awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import java.io.File
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.Before

class MealViewModelTest {

  private lateinit var mealViewModel: MealViewModel
  private val mealRepo: MealRepository = mockk(relaxed = true)
  private val apiCaller: SpoonacularApiCaller = mockk(relaxed = true)

  @Before
  fun setUp() {
    mealViewModel = MealViewModel("testUserID")
  }

  @Test
  fun getAllMealsReturnsExpectedMeals() = runTest {
    val meals = listOf(Meal.default(), Meal.default())
    coEvery { mealRepo.getAllMeals() } returns Tasks.forResult(meals)

    val observer = mockk<Observer<List<Meal?>>>(relaxed = true)
    mealViewModel.getAllMeals().observeForever(observer)

    coVerify { observer.onChanged(meals) }
  }

  @Test
  fun getAllMealsReturnsDefaultMealOnError() = runTest {
    coEvery { mealRepo.getAllMeals() } throws Exception()

    val observer = mockk<Observer<List<Meal?>>>(relaxed = true)
    mealViewModel.getAllMeals().observeForever(observer)

    coVerify { observer.onChanged(listOf(Meal.default())) }
  }

  @Test
  fun getMealsFromImageReturnsExpectedMeal() = runTest {
    val bitmap: Bitmap = mockk(relaxed = true)
    val apiResponse: ImageAnalysisResponseAPI = mockk(relaxed = true)
    val recipeInformation: RecipeNutritionResponseAPI = mockk(relaxed = true)
    coEvery { apiCaller.imageAnalysis(any<File>()) } returns apiResponse
    coEvery { apiCaller.getMealNutrition(any()) } returns recipeInformation

    val observer = mockk<Observer<Meal>>(relaxed = true)
    mealViewModel.getMealsFromImage(bitmap).observeForever(observer)

    coVerify { observer.onChanged(any()) }
  }

  @Test
  fun getMealsFromImageReturnsDefaultMealOnError() = runTest {
    val bitmap: Bitmap = mockk(relaxed = true)
    coEvery { apiCaller.imageAnalysis(any<File>()) } throws Exception()

    val observer = mockk<Observer<Meal>>(relaxed = true)
    mealViewModel.getMealsFromImage(bitmap).observeForever(observer)

    coVerify { observer.onChanged(Meal.default()) }
  }

  @Test
  fun setMealStoresMealInRepository() = runTest {
    val meal: Meal = mockk(relaxed = true)
    val expectedAwait: Awaits = mockk(relaxed = true)
    coEvery { mealRepo.storeMeal(meal) } just expectedAwait

    mealViewModel.setMeal(meal)

    coVerify { mealRepo.storeMeal(meal) }
  }
}
