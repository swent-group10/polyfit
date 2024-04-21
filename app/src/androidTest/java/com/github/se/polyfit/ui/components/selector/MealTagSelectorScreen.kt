package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class MealTagSelectorScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<MealTagSelectorScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("MealTagSelector") }) {

  val mealTagSelectorTitle: KNode = child { hasTestTag("Title") }
  private val mealTagBox: KNode = child { hasTestTag("MealTagBox") }
  private val mealTagList: KNode = mealTagBox.child { hasTestTag("MealTagList") }
  val mealTagItem: KNode = mealTagList.child { hasTestTag("MealTag") }
  val addTag: KNode = mealTagList.child { hasTestTag("AddTag") }

  val addMealTagDialog: KNode = onNode { hasTestTag("AddMealTagDialog") }
  private val gradientBox: KNode = onNode { hasTestTag("GradientBox") }
  private val mealTagContentContainer: KNode =
      gradientBox.child { hasTestTag("MealTagContentContainer") }
  val addMealTagTitleRow: KNode = mealTagContentContainer.child { hasTestTag("TitleRow") }
  val addMealTagTitle: KNode = addMealTagTitleRow.child { hasTestTag("Title") }
  val removeMealTagButton: KNode = addMealTagTitleRow.child { hasTestTag("RemoveMealTagButton") }
  val labelSubtitle: KNode = mealTagContentContainer.child { hasTestTag("Label") }
  val labelInput: KNode = mealTagContentContainer.child { hasTestTag("LabelInput") }
  val colorSubtitle: KNode = mealTagContentContainer.child { hasTestTag("Color") }
  val colorTable: KNode = mealTagContentContainer.child { hasTestTag("ColorTable") }
  val colorRow: KNode = colorTable.child { hasTestTag("ColorRow") }
  val colorButton: KNode = colorRow.child { hasTestTag("ColorButton") }
  val saveButton: KNode = onNode { hasTestTag("SaveButton") }
}
