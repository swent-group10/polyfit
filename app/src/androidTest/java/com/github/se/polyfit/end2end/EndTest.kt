package com.github.se.polyfit.end2end

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.github.se.polyfit.R
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.flow.AddMealFlow
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.screen.AdditionalMealInfoScreen
import com.github.se.polyfit.ui.screen.CreatePostScreen
import com.github.se.polyfit.ui.screen.FullGraphScreen
import com.github.se.polyfit.ui.screen.IngredientsBottomBar
import com.github.se.polyfit.ui.screen.OverviewScreen
import com.github.se.polyfit.ui.screen.PictureDialogBox
import com.github.se.polyfit.ui.utils.OverviewTags
import com.github.se.polyfit.ui.viewModel.GraphViewModel
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import com.github.se.polyfit.viewmodel.meal.OverviewViewModel
import com.github.se.polyfit.viewmodel.post.CreatePostViewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class EndTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule val mockkRule = MockKRule(this)

  @get:Rule
  val grantPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

  private val overviewViewModel: OverviewViewModel = mockk(relaxed = true)

  @Before
  fun SettingUp() {
    mockkStatic(Log::class)
    System.setProperty("isTestEnvironment", "true")

    every { overviewViewModel.storeMeal(any()) } returns 1L
  }

  @After
  fun tearDown() {
    unmockkStatic(Log::class)
  }

  fun setup() {
    val dataProcessor = mockk<LocalDataProcessor>(relaxed = true)
    val mockPostViewModel: CreatePostViewModel = mockk(relaxed = true)

    // Mock the behavior of the methods
    every { mockPostViewModel.getCarbs() } returns 0.0
    every { mockPostViewModel.getFat() } returns 0.0
    every { mockPostViewModel.getProtein() } returns 0.0
    every { mockPostViewModel.setPost() } just Runs

    every { mockPostViewModel.meals.value } returns listOf<Meal>(Meal.default())

    val mealViewModel = mockk<MealViewModel>(relaxed = true)
    mealViewModel.setMealData(Meal.default())

    val mockMeal = Meal.default()
    every { mealViewModel.meal } returns MutableStateFlow(mockMeal)

    composeTestRule.setContent {
      val navController = rememberNavController()
      NavHost(navController = navController, startDestination = Route.Home) {
        composable(Route.Home) {
          GenericScreen(
              navController = navController,
              content = { paddingValues ->
                OverviewScreen(paddingValues, navController, overviewViewModel)
              })
        }

        composable(Route.Graph) {
          FullGraphScreen(goBack = {}, viewModel = GraphViewModel(dataProcessor))
        }
        composable(Route.CreatePost) { CreatePostScreen(postViewModel = mockPostViewModel) }
        composable(Route.AddMeal + "/{mId}") { backStackEntry ->
          val mealId = backStackEntry.arguments?.getString("mId")?.toLong()
          AddMealFlow({}, {}, mealId = mealId, mealViewModel)
        }
      }
    }
  }

  @Test
  fun endToEndTest() {

    setup()

    // Click on the OverviewPictureBtn to open the PictureDialogBox
    ComposeScreen.onComposeScreen<OverviewScreen>(composeTestRule) {
      composeTestRule.onNodeWithTag(OverviewTags.overviewPictureBtn).performClick()
    }

    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    // Create a Bitmap from the drawable resource
    val bitmap = BitmapFactory.decodeResource(appContext.resources, R.drawable.google_logo)

    // Create a mock result for the picture intent
    val resultData = Intent()
    resultData.putExtra("data", bitmap)
    val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

    Intents.init()
    Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)

    // Click on the firstButton in the PictureDialogBox
    ComposeScreen.onComposeScreen<PictureDialogBox>(composeTestRule) {
      firstButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }
    Intents.release()

    // Go to Ingredient analyse and especially IngredientsBottomBar
    ComposeScreen.onComposeScreen<IngredientsBottomBar>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      // Click on doneButton
      doneButton {
        assertExists()
        assertIsDisplayed()
        assertHasClickAction()
        performClick()
      }
    }

    ComposeScreen.onComposeScreen<AdditionalMealInfoScreen>(composeTestRule) {
      assertExists()
      assertIsDisplayed()

      dateSelector {
        assertExists()
        assertIsDisplayed()
      }

      mealOccasionSelector {
        assertExists()
        assertIsDisplayed()
      }

      mealTagSelector {
        assertExists()
        assertIsDisplayed()
      }
    }

  }
}
