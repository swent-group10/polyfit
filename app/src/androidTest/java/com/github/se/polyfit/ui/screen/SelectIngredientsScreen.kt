package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class IngredientsTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("TopBar") }) {

  // Top Bar
  val ingredientTitle: KNode = child { hasTestTag("IngredientTitle") }
  val backButton: KNode = child { hasTestTag("BackButton") }
}

class IngredientsBottomBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsBottomBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BottomBar") }) {

  // Bottom Bar
  private val addIngredientBox: KNode = child { hasTestTag("AddIngredientBox") }
  val addIngredientButton: KNode = addIngredientBox.child { hasTestTag("AddIngredientButton") }
  private val doneBox: KNode = child { hasTestTag("DoneBox") }
  val doneButton: KNode = doneBox.child { hasTestTag("DoneButton") }
}

class IngredientsList(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsList>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("IngredientsList") }) {

  // Ingredients
  val ingredientScreen: KNode = child { hasTestTag("IngredientScreen") }

  // get all the elements with test tag ingredient
  val ingredient: List<KNode> = child { hasTestTag("Ingredient") }
  val morePotentialIngredients: KNode = child { hasTestTag("MoreIngredientsButton") }
}
