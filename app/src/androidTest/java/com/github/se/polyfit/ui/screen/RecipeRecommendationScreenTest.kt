package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.screen.recipeRec.RecommendationScreen
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeRecommendationScreenTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  private val barcodeViewModel: BarCodeCodeViewModel = mockk<BarCodeCodeViewModel>(relaxed = true)

  @Test
  fun testLoadingAnimation() {
    val mockkNavigation = mockk<NavHostController>(relaxed = true)

    val mockkRecommendationViewModel = mockk<RecipeRecommendationViewModel>()

    coEvery { mockkRecommendationViewModel.recipeFromIngredients() } returns emptyList()
    coEvery { mockkRecommendationViewModel.recipes } returns MutableLiveData(listOf())
    coEvery { mockkRecommendationViewModel.isFetching } returns MutableLiveData(true)
    composeTestRule.setContent {
      RecommendationScreen(mockkNavigation, mockkRecommendationViewModel, {}, barcodeViewModel)
    }

    ComposeScreen.onComposeScreen<RecipeRecommendationScreen>(composeTestRule) {
      composeTestRule.onNodeWithTag("Loader").assertIsDisplayed()
    }
  }
}
