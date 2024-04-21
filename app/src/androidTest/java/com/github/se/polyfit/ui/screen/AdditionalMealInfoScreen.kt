package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AdditionalMealInfoTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AdditionalMealInfoTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("TopBar") }) {

  // Top Bar
  val title: KNode = child { hasTestTag("Additional Meal Info Title") }
  val backButton: KNode = child { hasTestTag("BackButton") }
}

class AdditionalMealInfoBottomBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AdditionalMealInfoBottomBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BottomBar") }) {

  // Bottom Bar
  private val doneBox: KNode = child { hasTestTag("DoneBox") }
  val doneButton: KNode = doneBox.child { hasTestTag("DoneButton") }
}

class AdditionalMealInfoScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AdditionalMealInfoScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AdditionalMealInfoContainer") }) {

  val dateSelector: KNode = child { hasTestTag("DateSelector") }
  val mealOccasionSelector: KNode = child { hasTestTag("MealOccasionSelector") }
  val mealTagSelector: KNode = child { hasTestTag("MealTagSelector") }
}
