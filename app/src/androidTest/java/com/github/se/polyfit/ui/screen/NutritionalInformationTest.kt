// package com.github.se.polyfit.ui.screen
//
// import android.util.Log
// import androidx.compose.ui.test.assertIsDisplayed
// import androidx.compose.ui.test.junit4.createComposeRule
// import androidx.test.ext.junit.runners.AndroidJUnit4
// import com.github.se.polyfit.model.meal.Meal
// import com.github.se.polyfit.model.meal.MealOccasion
// import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
// import com.github.se.polyfit.ui.navigation.Navigation
// import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
// import io.github.kakaocup.compose.node.element.ComposeScreen
// import io.mockk.confirmVerified
// import io.mockk.impl.annotations.RelaxedMockK
// import io.mockk.junit4.MockKRule
// import io.mockk.mockkStatic
// import io.mockk.unmockkStatic
// import io.mockk.verify
// import org.junit.After
// import org.junit.Before
// import org.junit.Rule
// import org.junit.Test
// import org.junit.runner.RunWith
//
// @RunWith(AndroidJUnit4::class)
// class NutritionalInfoTest : TestCase() {
//  @get:Rule val composeTestRule = createComposeRule()
//
//  @get:Rule val mockkRule = MockKRule(this)
//
//  @RelaxedMockK lateinit var mockNav: Navigation
//
//  private val meal =
//      Meal(
//          name = "Steak and frites",
//          mealID = 1,
//          nutritionalInformation = NutritionalInformation(mutableListOf()),
//          occasion = MealOccasion.LUNCH)
//
//  @Before
//  fun setup() {
//    mockkStatic(Log::class)
//    composeTestRule.setContent { NutritionScreen(meal = meal, navigation = mockNav) }
//  }
//
//  @After
//  fun tearDown() {
//    unmockkStatic(Log::class)
//  }
//
//  @Test
//  fun topBarDisplayed() {
//    ComposeScreen.onComposeScreen<NutritionalInformationTopBar>(composeTestRule) {
//      title {
//        assertIsDisplayed()
//        assertTextEquals("Nutrition Facts")
//      }
//
//      backButton {
//        assertIsDisplayed()
//        assertHasClickAction()
//        assertContentDescriptionEquals("Back")
//        performClick()
//      }
//
//      verify { mockNav.goBack() }
//      confirmVerified(mockNav)
//    }
//  }
//
//  @Test
//  fun addRecipeButton() {
//    ComposeScreen.onComposeScreen<NutritionalInformationBottomBar>(composeTestRule) {
//      addToDiaryButton {
//        assertIsDisplayed()
//        assertHasClickAction()
//        assertTextEquals("Add to Diary")
//      }
//
//      addRecipeButton {
//        assertIsDisplayed()
//        assertHasClickAction()
//        assertTextEquals("Add Recipe")
//        performClick()
//      }
//
//      // TODO: Update with proper test when functionality is implemented
//      verify { Log.v("Add Recipe", "Clicked") }
//    }
//  }
//
//  @Test
//  fun addToDiaryButton() {
//    ComposeScreen.onComposeScreen<NutritionalInformationBottomBar>(composeTestRule) {
//      addRecipeButton {
//        assertIsDisplayed()
//        assertHasClickAction()
//        assertTextEquals("Add Recipe")
//      }
//
//      addToDiaryButton {
//        assertIsDisplayed()
//        assertHasClickAction()
//        assertTextEquals("Add to Diary")
//        performClick()
//      }
//
//      verify { Log.v("Add to Diary", "Clicked") }
//    }
//  }
//
//  @Test
//  fun verifyBodyShown() {
//    ComposeScreen.onComposeScreen<NutritionalInformationBody>(composeTestRule) {
//      mealName {
//        assertIsDisplayed()
//        assertTextEquals(meal.name)
//      }
//    }
//  }
// }
