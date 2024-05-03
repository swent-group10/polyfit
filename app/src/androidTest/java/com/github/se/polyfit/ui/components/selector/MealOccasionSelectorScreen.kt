package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MealOccasionSelectorScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MealOccasionSelectorScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("MealOccasionSelector") }) {

  // Meal Occasion Selector
  val mealOccasionSelectorTitle: KNode = child { hasTestTag("Title") }
  private val buttonRow: KNode = child { hasTestTag("ButtonRow") }
  private val leftColumn: KNode = buttonRow.child { hasTestTag("LeftColumn") }
  private val rightColumn: KNode = buttonRow.child { hasTestTag("RightColumn") }
  private val breakfastRow: KNode = leftColumn.child { hasTestTag("BreakfastRow") }
  val breakfastButton: KNode = breakfastRow.child { hasTestTag("BreakfastButton") }
  val breakfastText: KNode = breakfastRow.child { hasTestTag("BreakfastText") }
  private val lunchRow: KNode = rightColumn.child { hasTestTag("LunchRow") }
  val lunchButton: KNode = lunchRow.child { hasTestTag("LunchButton") }
  val lunchText: KNode = lunchRow.child { hasTestTag("LunchText") }
  private val dinnerRow: KNode = leftColumn.child { hasTestTag("DinnerRow") }
  val dinnerButton: KNode = dinnerRow.child { hasTestTag("DinnerButton") }
  val dinnerText: KNode = dinnerRow.child { hasTestTag("DinnerText") }
  private val snackRow: KNode = rightColumn.child { hasTestTag("SnackRow") }
  val snackButton: KNode = snackRow.child { hasTestTag("SnackButton") }
  val snackText: KNode = snackRow.child { hasTestTag("SnackText") }
}
