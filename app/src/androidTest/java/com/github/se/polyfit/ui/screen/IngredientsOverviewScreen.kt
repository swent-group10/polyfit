package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class IngredientsOverviewScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsOverviewScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("IngredientsOverviewScaffold") }) {

  val topBar: KNode = child { hasTestTag("TopBar") }
  val bottomBar: KNode = child { hasTestTag("GenerateBox") }
  val floatingActionButton: KNode = child { hasTestTag("FloatingButton") }
  val listProducts: KNode = child { hasTestTag("ListProductColumn") }
}

class IngredientsOverviewTopBarOld(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsOverviewTopBarOld>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("TopBarIngredient") }) {

  val backButton: KNode = child { hasTestTag("BackButton") }
  val title: KNode = child { hasTestTag("ProductTitle") }
  val icon: KNode = backButton.child { hasTestTag("BackButtonIcon") }
}

class IngredientsOverviewTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsOverviewTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("TopBar") }) {

  val title: KNode = child { hasTestTag("Product Title") }
  val backButton: KNode = child { hasTestTag("BackButton") }
}

class IngredientsOverviewBottomBarIngredient(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<IngredientsOverviewBottomBarIngredient>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("GenerateBox") }) {

  val generateButton: KNode = child { hasTestTag("GenerateButton") }
}

class FloatingActionButtonIngredientsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<FloatingActionButtonIngredientsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("FloatingButton") }) {

  val icon: KNode = child { hasTestTag("AddIcon") }
}

class ListProductsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ListProductsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("ListProductColumn") }) {

  val card: KNode = child { hasTestTag("ProductCard") }
  val productName: KNode = card.child { hasTestTag("ProductName") }

  val ServingSize: KNode = card.child { hasTestTag("ServingSize") }
  val ServiceSizet1: KNode = ServingSize.child { hasTestTag("TextIngredient") }
  val ServiceSizet2: KNode = ServingSize.child { hasTestTag("TextIngredientValue") }

  val calories: KNode = card.child { hasTestTag("calories") }
  val caloriest1: KNode = calories.child { hasTestTag("TextIngredient") }
  val caloriest2: KNode = calories.child { hasTestTag("TextIngredientValue") }

  val carbs: KNode = card.child { hasTestTag("carbs") }
  val carbst1: KNode = carbs.child { hasTestTag("TextIngredient") }
  val carbst2: KNode = carbs.child { hasTestTag("TextIngredientValue") }

  val fat: KNode = card.child { hasTestTag("fat") }
  val fatt1: KNode = fat.child { hasTestTag("TextIngredient") }
  val fatt2: KNode = fat.child { hasTestTag("TextIngredientValue") }

  val protein: KNode = card.child { hasTestTag("Protein") }
  val proteint1: KNode = protein.child { hasTestTag("TextIngredient") }
  val proteint2: KNode = protein.child { hasTestTag("TextIngredientValue") }
}
