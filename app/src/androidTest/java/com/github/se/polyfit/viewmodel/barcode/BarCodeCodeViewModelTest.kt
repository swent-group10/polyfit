package com.github.se.polyfit.viewmodel.barcode

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import com.github.se.polyfit.viewmodel.qrCode.MAX_BARCODE_LENGTH
import com.github.se.polyfit.viewmodel.qrCode.MIN_BARCODE_LENGTH
import com.github.se.polyfit.viewmodel.qrCode.REQUIRED_SCAN_COUNT
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class BarCodeCodeViewModelTest {

  @get:Rule var rule: TestRule = InstantTaskExecutorRule()

  private val spoonacularApiCaller: SpoonacularApiCaller =
      mockk<SpoonacularApiCaller>(relaxed = true)
  private val openFoodFactsApi: OpenFoodFactsApi = mockk<OpenFoodFactsApi>(relaxed = true)
  private val viewModel =
      BarCodeCodeViewModel(RecipeRecommendationViewModel(spoonacularApiCaller), openFoodFactsApi)

  @Before
  fun setup() {
    mockkStatic(Log::class)
    every { Log.v(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0
  }

  @AfterTest
  fun teardown() {
    unmockkAll()
  }

  @Test
  fun addindValidIdToList() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    Assert.assertEquals(listOf("123456"), viewModel.listId.value)
  }

  @Test
  fun addingNullId() {
    viewModel.addId(null)
    Assert.assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun ID_AlreadyInList() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    Assert.assertEquals(listOf("123456"), viewModel.listId.value)
  }

  @Test
  fun addingWrongSizeId() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345")
    }
    Assert.assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun addingIdAtTopOfIdList() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("789012")
    }
    Assert.assertEquals(listOf("789012", "123456"), viewModel.listId.value)
  }

  @Test
  fun addId_notAddingEmptyId() {

    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("")
    }
    Assert.assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun addId_notAddingIdTooLong() {
    var s = ""
    for (i in 1..MAX_BARCODE_LENGTH + 1) {
      s += "1"
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId(s)
    }
    Assert.assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun addId_notAddingIdToàShort() {
    var s = ""
    for (i in 1 ..< MIN_BARCODE_LENGTH) {
      s += "1"
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId(s)
    }
    Assert.assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun addId_notAddingEnoughTimes() {

    for (i in 1 ..< REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345678")
    }
    Assert.assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun addId_addingSameValue() {

    for (i in 1 ..< REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345678")
    }
    viewModel.addId(null)
    viewModel.addId("12345678")
    Assert.assertEquals(listOf("12345678"), viewModel.listId.value)
  }
}
