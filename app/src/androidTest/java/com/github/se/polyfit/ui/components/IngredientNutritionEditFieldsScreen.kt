package com.github.se.polyfit.ui.components

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class IngredientNutritionEditFieldsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientNutritionEditFieldsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("NutritionInfoContainer") }) {

  val servingSizeContainer: KNode = child { hasTestTag("NutritionInfoContainer Total Weight") }
  val caloriesContainer: KNode = child { hasTestTag("NutritionInfoContainer Calories") }
  val carbsContainer: KNode = child { hasTestTag("NutritionInfoContainer Carbohydrates") }
  val fatContainer: KNode = child { hasTestTag("NutritionInfoContainer Fat") }
  val proteinContainer: KNode = child { hasTestTag("NutritionInfoContainer Protein") }

  val servingSizeLabel: KNode =
      servingSizeContainer.child { hasTestTag("NutritionLabel Total Weight") }
  val caloriesLabel: KNode = caloriesContainer.child { hasTestTag("NutritionLabel Calories") }
  val carbsLabel: KNode = carbsContainer.child { hasTestTag("NutritionLabel Carbohydrates") }
  val fatLabel: KNode = fatContainer.child { hasTestTag("NutritionLabel Fat") }
  val proteinLabel: KNode = proteinContainer.child { hasTestTag("NutritionLabel Protein") }

  val servingSizeInput: KNode =
      servingSizeContainer.child { hasTestTag("NutritionSizeInput Total Weight") }
  val caloriesInput: KNode = caloriesContainer.child { hasTestTag("NutritionSizeInput Calories") }
  val carbsInput: KNode = carbsContainer.child { hasTestTag("NutritionSizeInput Carbohydrates") }
  val fatInput: KNode = fatContainer.child { hasTestTag("NutritionSizeInput Fat") }
  val proteinInput: KNode = proteinContainer.child { hasTestTag("NutritionSizeInput Protein") }

  val servingSizeUnit: KNode =
      servingSizeContainer.child { hasTestTag("NutritionUnit Total Weight") }
  val caloriesUnit: KNode = caloriesContainer.child { hasTestTag("NutritionUnit Calories") }
  val carbsUnit: KNode = carbsContainer.child { hasTestTag("NutritionUnit Carbohydrates") }
  val fatUnit: KNode = fatContainer.child { hasTestTag("NutritionUnit Fat") }
  val proteinUnit: KNode = proteinContainer.child { hasTestTag("NutritionUnit Protein") }
}
