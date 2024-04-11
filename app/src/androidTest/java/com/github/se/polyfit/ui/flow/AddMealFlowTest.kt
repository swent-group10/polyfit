package com.github.se.polyfit.ui.flow

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.screen.IngredientsBottomBar
import com.github.se.polyfit.ui.screen.IngredientsList
import com.github.se.polyfit.ui.screen.IngredientsTopBar
import com.github.se.polyfit.ui.screen.NutritionalInformationTopBar
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddMealFlowTest {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @RelaxedMockK lateinit var mockNav: Navigation

  @Before
  fun setup() {
    composeTestRule.setContent { AddMealFlow(mockNav, "testUserID") }
  }

  @Test
  fun IngredientScreenIsShown() {
    ComposeScreen.onComposeScreen<IngredientsTopBar>(composeTestRule) {
      ingredientTitle {
        assertIsDisplayed()
        assertTextEquals("Ingredients")
      }

      backButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertContentDescriptionEquals("Back")
        performClick()
      }

      verify { mockNav.goBack() }
      confirmVerified(mockNav)
    }
    ComposeScreen.onComposeScreen<IngredientsList>(composeTestRule) {
      ingredientButton { assertDoesNotExist() }
    }
  }

  @Test
  fun NutritionScreenIsShown() {
    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      doneButton {
        assertIsDisplayed()
        assertHasClickAction()
        assertTextEquals("Done")
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<NutritionalInformationTopBar>(composeTestRule) {
      title {
        assertIsDisplayed()
        assertTextEquals("Nutrition Facts")
      }
    }
  }
}
