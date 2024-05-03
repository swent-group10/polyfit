package com.github.se.polyfit.end2end

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.github.se.polyfit.R
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.model.meal.MealOccasion
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.IngredientsBottomBar
import com.github.se.polyfit.ui.screen.OverviewScreen
import com.github.se.polyfit.ui.screen.PictureDialogBox
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class EndTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @get:Rule
  val grantPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

  private val mealViewModel: MealViewModel = mockk<MealViewModel>(relaxed = true)

  @Before
  fun setup() {

    // Mock the behavior of the meal property
    val a: Meal =
        Meal(
            name = "Old Name",
            mealID = 123,
            mealTemp = 0.0,
            nutritionalInformation = NutritionalInformation(mutableListOf()),
            ingredients = mutableListOf(),
            firebaseId = "",
            createdAt = LocalDate.now(),
            tags = mutableListOf(),
            occasion = MealOccasion.BREAKFAST)
    every { mealViewModel.meal } returns MutableStateFlow(a)

    // Mock the behavior of the isComplete property
    every { mealViewModel.isComplete } returns MutableStateFlow(true)

    // Mock the behavior of the setMealData method
    every { mealViewModel.setMealData(a) } just Runs

    // Mock the behavior of the updateMealData method
    every {
      mealViewModel.updateMealData(any(), any(), any(), any(), any(), any(), any(), any())
    } just Runs

    // Mock the behavior of the setMeal method
    every { mealViewModel.setMeal() } just Runs

    // Mock the behavior of the addIngredient method
    every { mealViewModel.addIngredient(any()) } just Runs

    // Mock the behavior of the removeIngredient method
    every { mealViewModel.removeIngredient(any()) } just Runs

    // Mock the behavior of the addTag method
    every { mealViewModel.addTag(any()) } just Runs

    // Mock the behavior of the removeTag method
    every { mealViewModel.removeTag(any()) } just Runs

    // Mock the behavior of the reset method
    every { mealViewModel.reset() } just Runs

    mockkStatic(Log::class)
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
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

    // Set up the Compose content
    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Overview) {

        // composable(Route.Register) { LoginScreen({Log.d("Register", "Register go to ????")}) }

        composable(Route.AddMeal) {
          // make sure the create is clear

          // check reall created
          AddMealFlow({}, {}, mealViewModel)
        }

        navigation(startDestination = Route.Home, route = Route.Overview) {
          composable(Route.Home) {
            GenericScreen(
                navController = navController,
                content = { paddingValues ->
                  OverviewScreen(paddingValues, navController, MealViewModel(mockk()))
                })
          }
          composable(Route.Map) {
            GenericScreen(navController = navController, content = { Text("Map Screen") })
          }
          composable(Route.Settings) {
            GenericScreen(navController = navController, content = { Text("Settings Screen") })
          }
        }
      }
    }

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
}
