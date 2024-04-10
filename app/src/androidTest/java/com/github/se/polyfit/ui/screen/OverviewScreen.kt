package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class OverviewTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<OverviewTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("MainTopBar") }) {
  val title: KNode = child { hasTestTag("TopBarTitle") }
  val topBarBox: KNode = child { hasTestTag("TitleBox") }
}

class OverviewScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<OverviewScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("OverviewScreen") }) {
  val lazyColumn: KNode = child { hasTestTag("OverviewScreenLazyColumn") }
  val welcomeMessage: KNode = lazyColumn.child { hasTestTag("WelcomeMessage") }
  val calorieCard: KNode = lazyColumn.child { hasTestTag("CalorieCard") }
  val secondCard: KNode = lazyColumn.child { hasTestTag("SecondCard") }
  val thirdCard: KNode = lazyColumn.child { hasTestTag("ThirdCard") }
  val genericImage: KNode = secondCard.child { hasTestTag("GenericPicture") }
}

class CalorieCard(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CalorieCard>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("CalorieCard") }) {
  val title: KNode = child { hasTestTag("CalorieCardTitle") }
  val mealTracking: KNode = child { hasTestTag("MealTracking") }
  val photoButton: KNode = child { hasTestTag("PhotoButton") }
  val editButton: KNode = child { hasTestTag("EditButton") }
  val historyButton: KNode = child { hasTestTag("HistoryButton") }

  val calorieAmount: KNode = child { hasTestTag("CalorieAmount") }
  val calNumber: KNode = calorieAmount.child { hasTestTag("Number") }
  val calSlash: KNode = calorieAmount.child { hasTestTag("Slash") }
  val calGoal: KNode = calorieAmount.child { hasTestTag("Goal") }

  val caloriePerMeal: KNode = child { hasTestTag("CaloriePerMeal") }
  val breakfast: KNode = caloriePerMeal.child { hasTestTag("Breakfast") }
  val lunch: KNode = caloriePerMeal.child { hasTestTag("Lunch") }
  val dinner: KNode = caloriePerMeal.child { hasTestTag("Dinner") }
  val snacks: KNode = caloriePerMeal.child { hasTestTag("Snacks") }
}

class PictureDialogBox(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<PictureDialogBox>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("PictureDialog") }) {
  val row: KNode = child { hasTestTag("PictureDialogRow") }
  val title: KNode = row.child { hasTestTag("PictureDialogTitle") }

  val column: KNode = child { hasTestTag("PictureDialogColumn") }
  val firstButton: KNode = column.child { hasTestTag("FirstButton") }
  val secondButton: KNode = column.child { hasTestTag("SecondButton") }
}
