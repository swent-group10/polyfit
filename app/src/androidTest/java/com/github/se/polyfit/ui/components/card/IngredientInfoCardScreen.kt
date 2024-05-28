package com.github.se.polyfit.ui.components.card

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class IngredientInfoCardScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientInfoCardScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("IngredientsCard") })
