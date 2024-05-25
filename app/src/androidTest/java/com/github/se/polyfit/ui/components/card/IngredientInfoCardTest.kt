package com.github.se.polyfit.ui.components.card

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.recipe.RecipeInformation
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.mockk.junit4.MockKRule
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class IngredientInfoCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @Test
    fun testEverythingIsDisplayed() {

        composeTestRule.waitForIdle()

        composeTestRule.setContent { IngredientInfoCard(RecipeInformation.default().ingredients.first()) }

        ComposeScreen.onComposeScreen<IngredientInfoCardScreen>(composeTestRule) {
            assertExists()
            assertIsDisplayed()

            composeTestRule.onNodeWithText(Ingredient.default().name).assertIsDisplayed()

            composeTestRule.onNodeWithText(Ingredient.default().amountFormatted())
                .assertIsDisplayed()
        }
    }
}
