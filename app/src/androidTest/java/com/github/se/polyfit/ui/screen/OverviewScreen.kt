package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.github.se.polyfit.ui.utils.OverviewTags
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
  val welcomeMessage: KNode = lazyColumn.child { hasTestTag(OverviewTags.overviewWelcome) }
  val calorieCard: KNode = lazyColumn.child { hasTestTag(OverviewTags.overviewMain) }
  val secondCard: KNode = lazyColumn.child { hasTestTag("SecondCard") }
  val genericImage: KNode = secondCard.child { hasTestTag("GenericPicture") }
}

class CalorieCard(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CalorieCard>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag(OverviewTags.overviewMain) }) {
  val column: KNode = child { hasTestTag("MealColumn") }
  val title: KNode = column.child { hasTestTag(OverviewTags.overviewGoal) }
  val mealTracking: KNode = child { hasTestTag(OverviewTags.overviewTrack) }
  val photoButton: KNode = child { hasTestTag(OverviewTags.overviewPictureBtn) }
  val editButton: KNode = child { hasTestTag(OverviewTags.overviewManualBtn) }
  val historyButton: KNode = child { hasTestTag(OverviewTags.overviewDetailsBtn) }

  val calorieAmount: KNode = child { hasTestTag(OverviewTags.overviewCalorie) }
  val calNumber: KNode = calorieAmount.child { hasTestTag("Number") }
  val calGoal: KNode = calorieAmount.child { hasTestTag("Goal") }

  val caloriePerMeal: KNode = child { hasTestTag("CaloriePerMeal") }
  val breakfast: KNode = caloriePerMeal.child { hasTestTag(OverviewTags.overviewBreakfast) }
  val lunch: KNode = caloriePerMeal.child { hasTestTag(OverviewTags.overviewLunch) }
  val dinner: KNode = caloriePerMeal.child { hasTestTag(OverviewTags.overviewDinner) }
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
