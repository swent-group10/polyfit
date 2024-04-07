package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NutritionalInformationTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NutritionalInformationTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("TopBar") }) {

  val title: KNode = child { hasTestTag("Title") }
  val backButton: KNode = child { hasTestTag("BackButton") }
}

class NutritionalInformationBottomBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NutritionalInformationBottomBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BottomBar") }) {

  private val buttonColumn: KNode = child { hasTestTag("ButtonColumn") }
  val addRecipeButton: KNode = buttonColumn.child { hasTestTag("AddRecipeButton") }
  val addToDiaryButton: KNode = buttonColumn.child { hasTestTag("AddToDiaryButton") }
}

class NutritionalInformationBody(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NutritionalInformationBody>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("NutritionalInformation") }) {

  val mealName: KNode = child { hasTestTag("MealName") }
}
