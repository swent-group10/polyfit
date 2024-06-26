package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraXTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun displays_cameraX() {
    composeTestRule.setContent {
      CameraXScreen(
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()),
          padding = PaddingValues(0.dp))
    }

    ComposeScreen.onComposeScreen<CameraXScreenTest>(composeTestRule) {
      AndroidView {
        assertExists()
        assertIsDisplayed()
      }
    }
  }
}
