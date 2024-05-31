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
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.data.local.ingredientscanned.IngredientsScanned
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.components.IngredientsOverview.ListProducts
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.viewmodel.meal.OverviewViewModel
import com.github.se.polyfit.viewmodel.qrCode.BarCodeCodeViewModel
import com.github.se.polyfit.viewmodel.qrCode.REQUIRED_SCAN_COUNT
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientsOverviewTest {

  @get:Rule
  val grantPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

  val i1 = IngredientsScanned("Apple", 100.0, 52.0, 14.0, 0.0, 0.0)
  val i2 = IngredientsScanned("Banana", 100.0, 89.0, 23.0, 0.0, 1.0)
  val i3 = IngredientsScanned("Carrot", 100.0, 41.0, 10.0, 0.0, 1.0)
  val i4 = IngredientsScanned("Date", 100.0, 282.0, 75.0, 0.0, 2.0)
  val i5 = IngredientsScanned("Eggplant", 100.0, 25.0, 6.0, 0.0, 1.0)

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
    composeTestRule.setContent {
      IngredientsOverview(
          {},
          {},
          {},
          l1,
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()))
    }

    ComposeScreen.onComposeScreen<IngredientsOverviewScreen>(composeTestRule) {
      topBar { assertExists() }

      bottomBar { assertExists() }

      floatingActionButton { assertExists() }

      listProducts { assertExists() }
    }
  }

  @Test
  fun displays_top() {
    composeTestRule.setContent {
      IngredientsOverview(
          {},
          {},
          {},
          l1,
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()))
    }

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
    composeTestRule.setContent {
      IngredientsOverview(
          {},
          {},
          {},
          l1,
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()))
    }

    ComposeScreen.onComposeScreen<IngredientsOverviewBottomBarIngredient>(composeTestRule) {
      generateButton {
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun displays_floating_action_button() {
    composeTestRule.setContent {
      IngredientsOverview(
          {},
          {},
          {},
          l1,
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()))
    }

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
      IngredientsOverview(
          navigateBack,
          navigateForward,
          onClickFloatingButton,
          l1,
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()))
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
      IngredientsOverview(
          navigateBack,
          navigateForward,
          onClickFloatingButton,
          l1,
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()))
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
      IngredientsOverview(
          navigateBack,
          navigateForward,
          onClickFloatingButton,
          l1,
          BarCodeCodeViewModel(
              RecipeRecommendationViewModel(SpoonacularApiCaller()), OpenFoodFactsApi()))
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

  private val mockkGoBack: () -> Unit = mockk(relaxed = true)
  private val mockkGoForward: () -> Unit = mockk(relaxed = true)
  private val foodFactsApi: OpenFoodFactsApi = mockk<OpenFoodFactsApi>(relaxed = true)
  private val barCodeCodeViewModel =
      BarCodeCodeViewModel(
          RecipeRecommendationViewModel(SpoonacularApiCaller()), foodFactsApi = foodFactsApi)
  private val context = mockk<Context>(relaxed = true)
  private val nutellaCode: String = "3017624010701"
  private val nutellaTMP = IngredientsScanned("Nutella", 0.0, 0.0, 57.5, 230.0, 134.2)

  fun setup() {

    every { ActivityCompat.checkSelfPermission(context, any()) } returns
        PackageManager.PERMISSION_GRANTED

    every { foodFactsApi.getIngredient(any()) } returns
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
          IngredientsOverview(mockkGoBack, mockkGoForward, {}, emptyList(), barCodeCodeViewModel)
        }
      }
    }
  }

  @Test
  fun getIngredientTMP() = runBlocking {
    setup()
    for (i in 1..REQUIRED_SCAN_COUNT) {
      barCodeCodeViewModel.addId(nutellaCode)
    }
    barCodeCodeViewModel.getIngredients()

    // Adding a delay to allow asynchronous operations to complete
    delay(2000)

    assertEquals(listOf(nutellaTMP), barCodeCodeViewModel.listIngredients.value)
  }

  @Test
  fun showTree() {

    composeTestRule.setContent {
      IngredientsOverview(mockkGoBack, mockkGoForward, {}, emptyList(), barCodeCodeViewModel)
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
