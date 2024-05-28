package com.github.se.polyfit.ui.screen.recipeRec

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class RecipeRecommendationMoreDetailScreenScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) :
    ComposeScreen<RecipeRecommendationMoreDetailScreenScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("RecipeDetail") })
