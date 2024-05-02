package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MealSelectorScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MealSelectorScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("MealSelector") }) {

  val mealSelectorRow: KNode = child { hasTestTag("MealSelectorRow") }
  val searchMealBar: KNode = child { hasTestTag("SearchMealBar") }
  val mealDetails: KNode = child { hasTestTag("MealDetails") }
  val carbs: KNode = onNode { hasTestTag("Carbs") }
  val protein: KNode = onNode { hasTestTag("Protein") }
  val fat: KNode = onNode { hasTestTag("Fat") }
  val ingredient: KNode = onNode { hasTestTag("Ingredient") }
}
