package com.github.se.polyfit.ui.components

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasTestTag
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class NutritionalInformationScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<NutritionalInformationScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("NutritionalInformation") }) {

  val mealName: KNode = child { hasTestTag("MealName") }
  val noMealInfo: KNode = child { hasTestTag("NoNutritionalInformation") }
  val nutrient: KNode = child { hasTestTag("NutrientInfo") }
  val nutrientName: KNode = nutrient.child { hasTestTag("NutrientName") }
  val nutrientAmount: KNode = nutrient.child { hasTestTag("NutrientAmount") }
}
