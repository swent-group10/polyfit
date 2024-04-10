package com.github.se.polyfit.viewmodel.meal

import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.android.gms.tasks.Tasks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
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
      Meal(MealOccasion.DINNER, "name", 123, 20.3, NutritionalInformation(mutableListOf()))

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
    viewModel.getMealsFromImage(bitmap)
  }
}
