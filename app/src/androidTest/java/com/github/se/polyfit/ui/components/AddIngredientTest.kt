package com.github.se.polyfit.ui.components

import android.util.Log
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.screen.Ingredient
import com.github.se.polyfit.ui.screen.IngredientScreen
import com.github.se.polyfit.ui.screen.IngredientsBottomBar
import com.github.se.polyfit.ui.screen.IngredientsList
import com.github.se.polyfit.ui.screen.IngredientsTopBar
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.confirmVerified
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientTest : TestCase() {
    @get:Rule val composeTestRule = createComposeRule()

    @get:Rule val mockkRule = MockKRule(this)

    @RelaxedMockK lateinit var mockNav: Navigation

    @Before
    fun setup() {
        mockkStatic(Log::class)
        composeTestRule.setContent { AddIngredientDialog(onClickCloseDialog = { }) }
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun popupBoxIsDisplayed() {
        ComposeScreen.onComposeScreen<AddIngredientPopupBox>(composeTestRule) {
            addIngredientDialog {
                assertIsDisplayed()
            }
            closePopupIcon {
                assertIsDisplayed()
                assertHasClickAction()
            }
            addIngredientContent{
                assertIsDisplayed()
            }
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchIngredientIsDisplayed() {
        ComposeScreen.onComposeScreen<AddIngredientSearchBar>(composeTestRule) {
            searchResultContainer{
                assertDoesNotExist()
            }

            singleSearchResult {
                assertDoesNotExist()
            }

            // Search bar input
            composeTestRule
                .onNodeWithText("Find an Ingredient...") // find based on placeholder
                .assertExists("Placeholder text is not found.")
                .performTextInput("a")

            searchResultContainer {
                assertIsDisplayed()
                performScrollToIndex(1)
            }

            singleSearchResult {
                assertIsDisplayed()
            }

        }
    }
    @Test
    fun searchIngredientIsFunctional() {
        ComposeScreen.onComposeScreen<AddIngredientSearchBar>(composeTestRule) {
            // Search bar input
            composeTestRule
                .onNodeWithText("Find an Ingredient...") // find based on placeholder
                .performTextInput("apple")

            singleSearchResult {
                assertIsDisplayed()
                assertTextContains("Apple")
                assertHasClickAction()
                performClick()
            }

            composeTestRule
                .onNodeWithText("Apple")
                .assertIsDisplayed()

            singleSearchResult {
                assertDoesNotExist()
            }
        }
    }


    @Test
    fun nutritionInfoIsDisplayed() {
        ComposeScreen.onComposeScreen<AddIngredientEditNutritionInfo>(composeTestRule) {
            servingSizeContainer {
                assertIsDisplayed()
            }
            carloiesContainer {
                assertIsDisplayed()
            }
            carbsContainer {
                assertIsDisplayed()
            }
            fatContainer {
                assertIsDisplayed()
            }
            proteinContainer {
                assertIsDisplayed()
            }

            servingSizeLabel {
                assertIsDisplayed()
                assertTextContains("Serving Size")
            }
            carloiesLabel {
                assertIsDisplayed()
                assertTextContains("Calories")
            }
            carbsLabel {
                assertIsDisplayed()
                assertTextContains("Carbs")
            }
            fatLabel {
                assertIsDisplayed()
                assertTextContains("Fat")
            }
            proteinLabel {
                assertIsDisplayed()
                assertTextContains("Protein")
            }

            servingSizeInput {
                assertIsDisplayed()
                assertTextContains("0")
                performTextClearance()
                performTextInput("1")
                assertTextContains("1")
            }
            carloiesInput {
                assertIsDisplayed()
                assertTextContains("0")
                performTextClearance()
                performTextInput("1")
                assertTextContains("1")
            }
            carbsInput {
                assertIsDisplayed()
                assertTextContains("0")
                performTextClearance()
                performTextInput("1")
                assertTextContains("1")
            }
            fatInput {
                assertIsDisplayed()
                assertTextContains("0")
                performTextClearance()
                performTextInput("1")
                assertTextContains("1")
            }
            proteinInput {
                assertIsDisplayed()
                assertTextContains("0")
                performTextClearance()
                performTextInput("1")
                assertTextContains("1")
            }

            servingSizeUnit {
                assertIsDisplayed()
                assertTextContains("g")
            }
            carloiesUnit {
                assertIsDisplayed()
                assertTextContains("kcal")
            }
            carbsUnit {
                assertIsDisplayed()
                assertTextContains("g")
            }
            fatUnit {
                assertIsDisplayed()
                assertTextContains("g")
            }
            proteinUnit {
                assertIsDisplayed()
                assertTextContains("g")
            }
        }
    }

    @Test
    fun addButtonIsDisplayed() {
        ComposeScreen.onComposeScreen<AddIngredientButton>(composeTestRule) {
            addIngredientButton {
                assertIsDisplayed()
                assertTextContains("Add")
                assertHasClickAction()
            }
        }
    }
}