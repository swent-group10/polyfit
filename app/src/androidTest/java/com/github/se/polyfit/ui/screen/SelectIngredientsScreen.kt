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
  val addIngredientGradientButton: KNode =
      addIngredientButton.child { hasTestTag("GradientButton") }
  private val doneBox: KNode = child { hasTestTag("DoneBox") }
  val doneButton: KNode = doneBox.child { hasTestTag("DoneButton") }
}

class IngredientsList(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsList>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("IngredientsList") }) {

  // Ingredients
  val noIngredients: KNode = child { hasTestTag("NoIngredients") }
  val noIngredientsButton: KNode = noIngredients.child { hasTestTag("GradientButton") }

  // get all the elements with test tag ingredient
  private val ingredient: KNode = child { hasTestTag("Ingredient") }
  val ingredientButton: KNode = ingredient.child { hasTestTag("GradientButton") }

  private val potentialIngredient: KNode = child { hasTestTag("PotentialIngredient") }
  val potentialIngredientButton: KNode = potentialIngredient.child { hasTestTag("GradientButton") }

  private val morePotentialIngredients: KNode = child { hasTestTag("MoreIngredientsButton") }
  val morePotentialIngredientsButton: KNode =
      morePotentialIngredients.child { hasTestTag("GradientButton") }
}
