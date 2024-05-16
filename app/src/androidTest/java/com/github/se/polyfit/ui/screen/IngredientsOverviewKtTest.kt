package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.ui.components.button.FloatingActionButtonIngredients
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import io.mockk.verify

@RunWith(AndroidJUnit4::class)
class IngredientsOverviewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displays_tree(){
        composeTestRule.setContent {
            FloatingActionButtonIngredients {}
        }
        Log.i("abc", "printAllNode  ${composeTestRule.onRoot(useUnmergedTree = true).printToString()}")
    }

    @Test
    fun displays_top() {
        composeTestRule.setContent {
            IngredientsOverview({}, {}, {})
        }


        ComposeScreen.onComposeScreen<IngredientsOverviewTopBar>(composeTestRule) {
            backButton {
                assertIsDisplayed()
                assertHasClickAction()
            }
        }
    }

    @Test
    fun displays_bottom() {
        composeTestRule.setContent {
            IngredientsOverview({}, {}, {})
        }


        ComposeScreen.onComposeScreen<IngredientsOverviewBottomBar>(composeTestRule) {
            generateButton {
                assertIsDisplayed()
                assertHasClickAction()
            }
        }
    }

    @Test
    fun navigates_back_when_top_bar_is_clicked() {
        val navigateBack: () -> Unit = mockk()
        composeTestRule.setContent {
            IngredientsOverview(navigateBack, {}, {})
        }

        composeTestRule.onNode(hasTestTag("TopBar")).performClick()

        verify(exactly = 1) {navigateBack() }
    }

    @Test
    fun navigates_forward_when_bottom_bar_is_clicked() {
        val navigateForward: () -> Unit = mockk()
        composeTestRule.setContent {
            IngredientsOverview({}, navigateForward, {})
        }

        composeTestRule.onNode(hasTestTag("BottomBar")).performClick()


        verify(exactly = 1) {navigateForward() }
    }

    @Test
    fun performs_action_when_floating_action_button_is_clicked() {
        val onClickFloatingButton: () -> Unit = mockk()
        composeTestRule.setContent {
            IngredientsOverview({}, {}, onClickFloatingButton)
        }

        composeTestRule.onNode(hasTestTag("FloatingActionButton")).performClick()

        verify(exactly = 1) {onClickFloatingButton() }
    }
}
