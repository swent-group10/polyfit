package com.github.se.polyfit.ui.components

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

class AddIngredientSearchBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AddIngredientSearchBar>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AddIngredientContentContainer") }) {

  private val searchIngredientBar: KNode = child { hasTestTag("SearchIngredientBar") }
  val searchResultContainer: KNode =
      searchIngredientBar.child { hasTestTag("IngredientSearchScrollableList") }
  val singleSearchResult: KNode = searchResultContainer.child { hasClickAction() }
}

class AddIngredientEditNutritionInfo(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AddIngredientEditNutritionInfo>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AddIngredientContentContainer") }) {

  val servingSizeContainer: KNode = child { hasTestTag("NutritionInfoContainer Serving Size") }
  val carloiesContainer: KNode = child { hasTestTag("NutritionInfoContainer Calories") }
  val carbsContainer: KNode = child { hasTestTag("NutritionInfoContainer Carbs") }
  val fatContainer: KNode = child { hasTestTag("NutritionInfoContainer Fat") }
  val proteinContainer: KNode = child { hasTestTag("NutritionInfoContainer Protein") }

  val servingSizeLabel: KNode =
      servingSizeContainer.child { hasTestTag("NutritionLabel Serving Size") }
  val carloiesLabel: KNode = carloiesContainer.child { hasTestTag("NutritionLabel Calories") }
  val carbsLabel: KNode = carbsContainer.child { hasTestTag("NutritionLabel Carbs") }
  val fatLabel: KNode = fatContainer.child { hasTestTag("NutritionLabel Fat") }
  val proteinLabel: KNode = proteinContainer.child { hasTestTag("NutritionLabel Protein") }

  val servingSizeInput: KNode =
      servingSizeContainer.child { hasTestTag("NutritionSizeInput Serving Size") }
  val carloiesInput: KNode = carloiesContainer.child { hasTestTag("NutritionSizeInput Calories") }
  val carbsInput: KNode = carbsContainer.child { hasTestTag("NutritionSizeInput Carbs") }
  val fatInput: KNode = fatContainer.child { hasTestTag("NutritionSizeInput Fat") }
  val proteinInput: KNode = proteinContainer.child { hasTestTag("NutritionSizeInput Protein") }

  val servingSizeUnit: KNode =
      servingSizeContainer.child { hasTestTag("NutritionUnit Serving Size") }
  val carloiesUnit: KNode = carloiesContainer.child { hasTestTag("NutritionUnit Calories") }
  val carbsUnit: KNode = carbsContainer.child { hasTestTag("NutritionUnit Carbs") }
  val fatUnit: KNode = fatContainer.child { hasTestTag("NutritionUnit Fat") }
  val proteinUnit: KNode = proteinContainer.child { hasTestTag("NutritionUnit Protein") }
}

class AddIngredientButton(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AddIngredientButton>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AddIngredientContentContainer") }) {

  val addIngredientButton: KNode = child { hasTestTag("PrimaryPurpleButton") }
}
