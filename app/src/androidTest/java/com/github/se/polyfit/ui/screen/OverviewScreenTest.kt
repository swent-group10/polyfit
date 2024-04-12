package com.github.se.polyfit.ui.screen

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.navigation.globalNavigation
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverviewTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @get:Rule
  val grantPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

  @Before
  fun setup() {
    // val mealViewModel = mockk<MealViewModel>()

    // Create a mock of MealViewModel
    val mealViewModel = mockk<MealViewModel>(relaxed = true)

    // Mock the behavior of the meal property
    val a: com.github.se.polyfit.model.meal.Meal =
        com.github.se.polyfit.model.meal.Meal(
            name = "Old Name",
            mealID = 123,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            occasion = MealOccasion.BREAKFAST)
    every { mealViewModel.meal } returns MutableLiveData(a)

    // Mock the behavior of the isComplete property
    every { mealViewModel.isComplete } returns MutableLiveData(true)

    // Mock the behavior of the setMealData method
    every { mealViewModel.setMealData(any()) } just Runs

    // Mock the behavior of the setMealName method
    every { mealViewModel.setMealName(any()) } just Runs

    // Mock the behavior of the setMeal method
    every { mealViewModel.setMeal() } just Runs

    // Mock the behavior of the addIngredient method
    every { mealViewModel.addIngredient(any()) } just Runs

    // Mock the behavior of the removeIngredient method
    every { mealViewModel.removeIngredient(any()) } just Runs

    // Now you can use mealViewModel in your tests

    // val navigation = mockk<Navigation>()

    mockkStatic(Log::class)
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Overview) {

        // composable(Route.Register) { LoginScreen(navigation::navigateToHome) }

        composable(Route.AddMeal) {
          // make sure the create is clear

          // check reall created
          AddMealFlow({}, {}, "testUserID", mealViewModel)
        }
        globalNavigation(navController, mealViewModel)
      }
    }
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  @Test
  fun topBarDisplayed() {
    composeTestRule.onNodeWithTag("MainTopBar").assertExists().assertIsDisplayed()
    composeTestRule.onNodeWithTag("topBarOuterBox").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewTitle)
        .assertExists()
        .assertIsDisplayed()
        .assertTextEquals("Polyfit")
  }

  @Test
  fun bottomBarDisplayed() {
    composeTestRule.onNodeWithTag("MainBottomBar").assertExists().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewHomeBtn)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewMapBtn)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithTag(OverviewTags.overviewMapBtn)
        .assertExists()
        .assertIsDisplayed()
        .assertHasClickAction()
    composeTestRule
        .onNodeWithContentDescription(OverviewTags.overviewHomeIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithContentDescription(OverviewTags.overviewMapIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()
    composeTestRule
        .onNodeWithContentDescription(OverviewTags.overviewSettingsIcon, useUnmergedTree = true)
        .assertExists()
        .assertIsDisplayed()

    composeTestRule
        .onNodeWithTag(OverviewTags.overviewHomeLabel, useUnmergedTree = true)
        .assertExists()

    composeTestRule
        .onNodeWithTag(OverviewTags.overviewMapLabel, useUnmergedTree = true)
        .assertDoesNotExist()

    composeTestRule
        .onNodeWithTag(OverviewTags.overviewSettingsLabel, useUnmergedTree = true)
        .assertDoesNotExist()
  }

  @Test
  fun OverviewScreenContent_Displayed() {
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
      secondCard {
        assertExists()
        assertIsDisplayed()
      }

      genericImage {
        assertExists()
        assertIsDisplayed()
      }

      calorieCard {
        assertExists()
        assertIsDisplayed()
      }

      welcomeMessage {
        assertExists()
        assertIsDisplayed()
      }
      ComposeScreen.onComposeScreen<CalorieCard>(composeTestRule) {
        assertExists()
        assertIsDisplayed()

        composeTestRule.onNodeWithTag(OverviewTags.overviewGoal).assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithTag(OverviewTags.overviewTrack).assertExists().assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(OverviewTags.overviewPictureBtn)
            .onChild()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithTag(OverviewTags.overviewManualBtn)
            .onChild()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule
            .onNodeWithTag(OverviewTags.overviewDetailsBtn)
            .onChild()
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()

        composeTestRule.onNodeWithContentDescription("photoIcon").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("penIcon").assertExists().assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription("historyIcon")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(OverviewTags.overviewCalorie)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("CaloriePerMeal").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithTag("Number").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithTag("Goal").assertExists().assertIsDisplayed()
      }
    }
  }

  @Test
  fun PictureDialog_Displays() {
    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      assertDoesNotExist()
      composeTestRule.onNodeWithTag(OverviewTags.overviewPictureBtn).performClick()
      assertExists()
      assertIsDisplayed()
      firstButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
      }
      secondButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
      }
    }
  }

  @Test
  fun testEndToEndCamera() {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    // Create a Bitmap from the drawable resource
    val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.google_logo)

    // Create a mock result for the picture intent
    val resultData = Intent()
    resultData.putExtra("data", bitmap)
    val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

    // Now you can use mealViewModel in your tests

    // Tell Espresso to expect an Intent to the camera, but respond with the mock result
    Intents.init()
    Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      assertDoesNotExist()
      composeTestRule.onNodeWithTag(OverviewTags.overviewPictureBtn).performClick()
      assertExists()
      assertIsDisplayed()
      firstButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
    Intents.release()

    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      doneButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
  }

  @Ignore("need to check the intent of storage system")
  @Test
  fun testEndToEndStockage() {

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    // Create a Bitmap from the drawable resource
    val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.google_logo)

    // Create a mock result for the picture intent
    val resultData = Intent()
    resultData.putExtra("data", bitmap)
    val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

    // Now you can use mealViewModel in your tests

    // Tell Espresso to expect an Intent to the camera, but respond with the mock result
    Intents.init()
    Intents.intending(IntentMatchers.hasAction(MediaStore.INTENT_ACTION_MEDIA_SEARCH))
        .respondWith(result)

    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      assertDoesNotExist()
      composeTestRule.onNodeWithTag(OverviewTags.overviewPictureBtn).performClick()
      assertExists()
      assertIsDisplayed()
      secondButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
    Intents.release()

    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      doneButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
  }
}
