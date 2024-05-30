package com.github.se.polyfit.ui.screen

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.components.IngredientsOverview.ListProducts
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.viewmodel.meal.OverviewViewModel
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientsOverviewTest {

  @get:Rule
  val grantPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

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
    composeTestRule.setContent { IngredientsOverview({}, {}, l1) }
    Log.i("abc", "printAllNode  ${composeTestRule.onRoot(useUnmergedTree = true).printToString()}")
  }

  @Test
  fun displays_ingredients_overview() {
    composeTestRule.setContent { IngredientsOverview({}, {}, l1, BarCodeCodeViewModel()) }

    ComposeScreen.onComposeScreen<IngredientsOverviewScreen>(composeTestRule) {
      topBar { assertExists() }

      bottomBar { assertExists() }

      listProducts { assertExists() }
    }
  }

  @Test
  fun displays_top() {
    composeTestRule.setContent { IngredientsOverview({}, {}, l1, BarCodeCodeViewModel()) }

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
    composeTestRule.setContent { IngredientsOverview({}, {}, l1, BarCodeCodeViewModel()) }

    ComposeScreen.onComposeScreen<IngredientsOverviewBottomBarIngredient>(composeTestRule) {
      generateButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
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
    composeTestRule.setContent {
      IngredientsOverview(navigateBack, navigateForward, l1, BarCodeCodeViewModel())
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
  }

  @Test
  fun navigates_forward() {
    val navigateBack: () -> Unit = mockk(relaxed = true)
    val navigateForward: () -> Unit = mockk(relaxed = true)
    composeTestRule.setContent {
      IngredientsOverview(navigateBack, navigateForward, l1, BarCodeCodeViewModel())
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
  }

  private val mockkGoBack: () -> Unit = mockk(relaxed = true)
  private val mockkGoForward: () -> Unit = mockk(relaxed = true)
  private val barCodeCodeViewModel = BarCodeCodeViewModel()
  private val context = mockk<Context>(relaxed = true)

  fun setup() {

    every { ActivityCompat.checkSelfPermission(context, any()) } returns
        PackageManager.PERMISSION_GRANTED

    System.setProperty("isTestEnvironment", "true")
    val mockkDataProcessor: LocalDataProcessor = mockk(relaxed = true)
    val mockkOverviewModule =
        OverviewViewModel(
            mockk(),
            mockk(),
            User.testUser().apply { displayName = "It's a me Mario" },
            mockkDataProcessor)

    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Home) {
        composable(Route.Home) {
          GenericScreen(
              navController = navController,
              content = { paddingValues ->
                OverviewScreen(paddingValues, navController, mockkOverviewModule)
              })
        }
        composable(Route.OverviewScan) {
          IngredientsOverview(mockkGoBack, mockkGoForward, emptyList(), barCodeCodeViewModel)
        }
      }
    }
  }

  @Test
  fun showTree() {

    composeTestRule.setContent {
      IngredientsOverview(mockkGoBack, mockkGoForward, emptyList(), barCodeCodeViewModel)
    }

    Log.i("abc", "printAllNode  ${composeTestRule.onRoot(useUnmergedTree = true).printToString()}")
  }

  @Test
  fun Verify_button_go_to_scan_screen_and_go_back() {
    setup()
    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      assertDoesNotExist()
      composeTestRule.onNodeWithTag(OverviewTags.overviewPictureBtn).performClick()
      assertExists()
      assertIsDisplayed()

      thirdButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<IngredientsOverviewTopBar>(composeTestRule) {
      assertExists()

      backButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    verify(exactly = 0) { mockkGoForward() }
    verify(exactly = 1) { mockkGoBack() }
  }

  @Test
  fun Verify_button_go_to_scan_and_go_further() {
    setup()
    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      assertDoesNotExist()
      composeTestRule.onNodeWithTag(OverviewTags.overviewPictureBtn).performClick()
      assertExists()
      assertIsDisplayed()

      thirdButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<IngredientsOverviewBottomBarIngredient>(composeTestRule) {
      assertExists()

      generateButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    verify(exactly = 1) { mockkGoForward() }
    verify(exactly = 0) { mockkGoBack() }
  }
}
