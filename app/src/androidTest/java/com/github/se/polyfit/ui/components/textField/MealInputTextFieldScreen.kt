package com.github.se.polyfit.ui.components.textField

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MealInputTextFieldScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MealInputTextFieldScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("SearchMealBar") }) {

  val searchMealBar: KNode = onNode { hasTestTag("SearchMealBar") }
  val placeholder: KNode = searchMealBar.child { hasText("Enter a meal…") }
  val scrollableList: KNode = onNode { hasTestTag("MealSearchScrollableList") }
  val meal: KNode = onNode { hasTestTag("Meal") }
  val mealName: KNode = meal.child { hasTestTag("MealName") }
  val mealCalories: KNode = meal.child { hasTestTag("MealCalories") }
}
