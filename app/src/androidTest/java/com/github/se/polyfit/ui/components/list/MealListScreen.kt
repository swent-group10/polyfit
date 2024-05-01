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
  val mealName: KNode = onNode { hasTestTag("MealName") }
  val mealCalories: KNode = onNode { hasTestTag("MealCalories") }
  val mealTagList: KNode = onNode { hasTestTag("MealTagList") }
}
