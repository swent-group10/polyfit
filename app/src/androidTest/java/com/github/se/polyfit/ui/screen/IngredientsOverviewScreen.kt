package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class IngredientsOverviewTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsOverviewTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("TopBar") }) {

  val backButton: KNode = child { hasTestTag("BackButton") }
}

class IngredientsOverviewBottomBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsOverviewBottomBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BottomBar") }) {

  val generateButton: KNode = child { hasTestTag("GenerateButton") }
}

class IngredientsOverviewScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsOverviewScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("IngredientsOverviewScalffold") }) {

  val listProductColumn: KNode = child { hasTestTag("ListProductColumn") }
}

class FloatingButtonScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
        ComposeScreen<FloatingButtonScreen>(
                semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("FloatingButton") }) {

  val addButton: KNode = child { hasContentDescription("Add Ingredient") }
}

class FloatingActionButtonScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
        ComposeScreen<FloatingActionButtonScreen>(
                semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("FloatingButton") }) {

  val addButton: KNode = child { hasContentDescription("Add Ingredient") }
}
