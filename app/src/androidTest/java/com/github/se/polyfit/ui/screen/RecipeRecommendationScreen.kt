package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class RecipeRecommendationScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<RecipeRecommendationScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("RecipeDisplay") })
