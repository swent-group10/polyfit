package com.github.se.polyfit.ui.components.list

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MealListScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MealListScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("MealList") }) {

  val occasionTitle: KNode = onNode { hasTestTag("OccasionTitle") }
  val totalCalories: KNode = onNode { hasTestTag("TotalCalories") }

  val mealCard: KNode = onNode { hasTestTag("MealCard") }
}
