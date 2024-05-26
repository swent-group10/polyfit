package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.components.IngredientsOverview.ListProducts
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientsOverviewTest {

  val i1 = IngredientsTMP("Apple", 100, 52, 14, 0, 0)
  val i2 = IngredientsTMP("Banana", 100, 89, 23, 0, 1)
  val i3 = IngredientsTMP("Carrot", 100, 41, 10, 0, 1)
  val i4 = IngredientsTMP("Date", 100, 282, 75, 0, 2)
  val i5 = IngredientsTMP("Eggplant", 100, 25, 6, 0, 1)

  val l1 = listOf(i1)
  val l2 = listOf(i1, i2, i3, i4, i5)

  @get:Rule val composeTestRule = createComposeRule()


  @Ignore("Not a test but useful to show the tree")
  @Test
  fun displays_tree() {
    composeTestRule.setContent { IngredientsOverview({}, {}, {}, l1) }
    Log.i("abc", "printAllNode  ${composeTestRule.onRoot(useUnmergedTree = true).printToString()}")
  }

  @Test
  fun displays_ingredients_overview() {
    composeTestRule.setContent { IngredientsOverview({}, {}, {}, l1, BarCodeCodeViewModel()) }

    ComposeScreen.onComposeScreen<IngredientsOverviewScreen>(composeTestRule) {
      topBar { assertExists() }

      bottomBar { assertExists() }

      floatingActionButton { assertExists() }

      listProducts { assertExists() }
    }
  }

  @Test
  fun displays_top() {
    composeTestRule.setContent { IngredientsOverview({}, {}, {}, l1, BarCodeCodeViewModel()) }

    ComposeScreen.onComposeScreen<IngredientsOverviewTopBar>(composeTestRule) {
      assertExists()
      assertIsDisplayed()
      backButton {
        assertIsDisplayed()
        assertHasClickAction()
      }

      title {
        assertIsDisplayed()
        assertTextContains("Product", ignoreCase = true, substring = true)
      }
    }
  }

  @Test
  fun displays_bottom() {
    composeTestRule.setContent { IngredientsOverview({}, {}, {}, l1, BarCodeCodeViewModel()) }

    ComposeScreen.onComposeScreen<IngredientsOverviewBottomBarIngredient>(composeTestRule) {
      generateButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun displays_floating_action_button() {
    composeTestRule.setContent { IngredientsOverview({}, {}, {}, l1, BarCodeCodeViewModel()) }

    ComposeScreen.onComposeScreen<FloatingActionButtonIngredientsScreen>(composeTestRule) {
      assertExists()
      assertIsDisplayed()
    }
  }

  @Test
  fun displayListProducts() {
    composeTestRule.setContent { ListProducts(l1, Modifier) }
    Log.i("abc", "printAllNode2  ${composeTestRule.onRoot(useUnmergedTree = true).printToString()}")

    ComposeScreen.onComposeScreen<ListProductsScreen>(composeTestRule) {
      card {
        assertExists()
        assertIsDisplayed()
      }

      productName {
        assertIsDisplayed()
        assertTextContains("Apple", ignoreCase = true, substring = true)
      }

      ServingSize { assertIsDisplayed() }

      ServiceSizet1 {
        assertIsDisplayed()
        assertTextContains("Serving Size", ignoreCase = true, substring = true)
      }

      ServiceSizet2 {
        assertIsDisplayed()
        assertTextContains("100", ignoreCase = true, substring = true)
        assertTextContains("g", ignoreCase = true, substring = true)
      }

      calories { assertIsDisplayed() }

      caloriest1 {
        assertIsDisplayed()
        assertTextContains("Calories", ignoreCase = true, substring = true)
      }

      caloriest2 {
        assertIsDisplayed()
        assertTextContains("52", ignoreCase = true, substring = true)
        assertTextContains("kcal", ignoreCase = true, substring = true)
      }

      carbs { assertIsDisplayed() }

      carbst1 {
        assertIsDisplayed()
        assertTextContains("Carbs", ignoreCase = true, substring = true)
      }

      carbst2 {
        assertIsDisplayed()
        assertTextContains("14", ignoreCase = true, substring = true)
        assertTextContains("g", ignoreCase = true, substring = true)
      }

      fat { assertIsDisplayed() }

      fatt1 {
        assertIsDisplayed()
        assertTextContains("Fat", ignoreCase = true, substring = true)
      }

      fatt2 {
        assertIsDisplayed()
        assertTextContains("0", ignoreCase = true, substring = true)
        assertTextContains("g", ignoreCase = true, substring = true)
      }

      protein { assertIsDisplayed() }

      proteint1 {
        assertIsDisplayed()
        assertTextContains("Protein", ignoreCase = true, substring = true)
      }

      proteint2 {
        assertIsDisplayed()
        assertTextContains("0", ignoreCase = true, substring = true)
        assertTextContains("g", ignoreCase = true, substring = true)
      }
    }
  }

  @Test
  fun navigates_back_when_top_bar_is_clicked() {
    val navigateBack: () -> Unit = mockk(relaxed = true)
    val navigateForward: () -> Unit = mockk(relaxed = true)
    val onClickFloatingButton: () -> Unit = mockk(relaxed = true)
    composeTestRule.setContent {
      IngredientsOverview(navigateBack, navigateForward, onClickFloatingButton, l1, BarCodeCodeViewModel())
    }

    ComposeScreen.onComposeScreen<IngredientsOverviewTopBar>(composeTestRule) {
      backButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    verify(exactly = 1) { navigateBack() }
    verify(exactly = 0) { navigateForward() }
    verify(exactly = 0) { onClickFloatingButton() }
  }

  @Test
  fun navigates_forward() {
    val navigateBack: () -> Unit = mockk(relaxed = true)
    val navigateForward: () -> Unit = mockk(relaxed = true)
    val onClickFloatingButton: () -> Unit = mockk(relaxed = true)
    composeTestRule.setContent {
      IngredientsOverview(navigateBack, navigateForward, onClickFloatingButton, l1, BarCodeCodeViewModel())
    }

    ComposeScreen.onComposeScreen<IngredientsOverviewBottomBarIngredient>(composeTestRule) {
      generateButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    verify(exactly = 0) { navigateBack() }
    verify(exactly = 1) { navigateForward() }
    verify(exactly = 0) { onClickFloatingButton() }
  }

  @Test
  fun perfom_click_floating_button() {
    val navigateBack: () -> Unit = mockk(relaxed = true)
    val navigateForward: () -> Unit = mockk(relaxed = true)
    val onClickFloatingButton: () -> Unit = mockk(relaxed = true)
    composeTestRule.setContent {
      IngredientsOverview(navigateBack, navigateForward, onClickFloatingButton, l1, mockk(relaxed = true))
    }

    ComposeScreen.onComposeScreen<IngredientsOverviewScreen>(composeTestRule) {
      floatingActionButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
    verify(exactly = 0) { navigateBack() }
    verify(exactly = 0) { navigateForward() }
    verify(exactly = 1) { onClickFloatingButton() }
  }
}
