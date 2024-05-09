package com.github.se.polyfit.ui.recipe

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class RecipeCardScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<RecipeCardScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("RecipeCard") })
