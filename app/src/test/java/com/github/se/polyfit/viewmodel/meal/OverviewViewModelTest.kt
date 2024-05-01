package com.github.se.polyfit.viewmodel.meal

import android.graphics.Bitmap
import android.util.Log
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.model.meal.Meal
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class OverviewViewModelTest {

  private val mockMealDao: MealDao = mockk()
  private val mockSpoonacularApiCaller: SpoonacularApiCaller = mockk()

  private lateinit var overviewViewModel: OverviewViewModel

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.e(any(), any()) } returns 0
    overviewViewModel = OverviewViewModel(mockMealDao, mockSpoonacularApiCaller)
  }

  @Test
  fun storeMeal_withNonNullBitmap_storesMeal() {
    val bitmap: Bitmap = mockk()
    val meal: Meal = mockk()
    every { mockSpoonacularApiCaller.getMealsFromImage(bitmap) } returns meal
    every { mockMealDao.insert(meal) } returns 1
    Assert.assertEquals(1.toLong(), overviewViewModel.storeMeal(bitmap))
  }

  @Test
  fun storeMeal_withNullBitmap_returnsNull() {
    val result = overviewViewModel.storeMeal(null)

    Assert.assertNull(result)
  }

  @Test
  fun deleteByDBId_deletesMeal() {
    val id = 1L
    every { mockMealDao.deleteByDatabaseID(id) } returns Unit

    // Call the method under test
    overviewViewModel.deleteByDBId(id)

    // Verify that the method was called with the correct parameters
    verify { mockMealDao.deleteByDatabaseID(id) }
  }
}
