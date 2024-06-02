package com.github.se.polyfit.ui.components.dialog

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AddIngredientPopupBox(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AddIngredientPopupBox>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AddIngredientPopupContainer") }) {

  val addIngredientDialog: KNode = child { hasTestTag("GradientBox") }
  val closePopupIcon: KNode = child { hasTestTag("TopRightIconInGradientBox") }
  val addIngredientContent: KNode =
      addIngredientDialog.child { hasTestTag("AddIngredientContentContainer") }
}

class AddIngredientEditNutritionInfo(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AddIngredientEditNutritionInfo>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("NutritionInfoContainer") }) {

  val servingSizeContainer: KNode = child { hasTestTag("NutritionInfoContainer Total Weight") }
  val caloriesContainer: KNode = child { hasTestTag("NutritionInfoContainer Calories") }
  val carbsContainer: KNode = child { hasTestTag("NutritionInfoContainer Carbohydrates") }
  val fatContainer: KNode = child { hasTestTag("NutritionInfoContainer Fat") }
  val proteinContainer: KNode = child { hasTestTag("NutritionInfoContainer Protein") }
}

class AddIngredientButton(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AddIngredientButton>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AddIngredientContentContainer") }) {

  val addIngredientButton: KNode = child { hasTestTag("PrimaryButton") }
  val ingredientTitle: KNode = child { hasTestTag("EnterIngredientName") }
}
