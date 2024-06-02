package com.github.se.polyfit.ui.screen.recipeRec

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class MoreDetailScreenScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MoreDetailScreenScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("RecipeDetail") })
