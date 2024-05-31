package com.github.se.polyfit.viewmodel.qrCode

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.screen.IngredientsTMP
import io.mockk.*
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class BarCodeCodeViewModelTest {

  @get:Rule var rule: TestRule = InstantTaskExecutorRule()

  private val viewModel = BarCodeCodeViewModel()
  private val nutellaCode: String = "3017624010701"
  private val nutellaTMP = IngredientsTMP("Nutella", 0.0, 0.0, 57.5, 230.0, 134.2)

  @Before
  fun setup() {
    mockkStatic(Log::class)
    val foodFactsApi = mockk<OpenFoodFactsApi>(relaxed = true)
    every { foodFactsApi.getIngredient(nutellaCode) } returns
        Ingredient(
            "Nutella",
            0,
            0.0,
            MeasurementUnit.G,
            NutritionalInformation(
                mutableListOf(
                    Nutrient("fat", 230.0, MeasurementUnit.G),
                    Nutrient("carbohydrates", 57.5, MeasurementUnit.G),
                    Nutrient("sugar", 56.3, MeasurementUnit.G),
                    Nutrient("protein", 134.2, MeasurementUnit.G))))
    every { Log.v(any(), any()) } returns 0
    every { Log.i(any(), any()) } returns 0
    every { Log.e(any(), any()) } returns 0
  }

  @AfterTest
  fun teardown() {
    unmockkAll()
  }

  @Test
  fun `addId adds id to list when id is valid and not in list`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    assertEquals(listOf("123456"), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id is null`() {
    viewModel.addId(null)
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id is already in list`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    assertEquals(listOf("123456"), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id length is not in 6 to 13`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345")
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId adds id to the top of the list when id is valid and not in list`() {
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("123456")
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("789012")
    }
    assertEquals(listOf("789012", "123456"), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id is empty`() {

    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId("")
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id length is more than 13`() {
    var s = ""
    for (i in 1..MAX_BARCODE_LENGTH + 1) {
      s += "1"
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId(s)
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not add id to list when id length is less than 6`() {
    var s = ""
    for (i in 1 ..< MIN_BARCODE_LENGTH) {
      s += "1"
    }
    for (i in 1..REQUIRED_SCAN_COUNT) {
      viewModel.addId(s)
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId does not added enough time`() {

    for (i in 1 ..< REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345678")
    }
    assertEquals(emptyList<String>(), viewModel.listId.value)
  }

  @Test
  fun `addId null and then add same value`() {

    for (i in 1 ..< REQUIRED_SCAN_COUNT) {
      viewModel.addId("12345678")
    }
    viewModel.addId(null)
    viewModel.addId("12345678")
    assertEquals(listOf("12345678"), viewModel.listId.value)
  }
}
