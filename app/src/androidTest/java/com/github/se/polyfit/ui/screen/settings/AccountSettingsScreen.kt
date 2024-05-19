package com.github.se.polyfit.ui.screen.settings

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class AccountSettingsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AccountSettingsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("AccountSettingsScreen") }) {

  val displayName: KNode = child { hasTestTag("DisplayName") }
  val firstName: KNode = child { hasTestTag("FirstName") }
  val lastName: KNode = child { hasTestTag("LastName") }
  val height: KNode = child { hasTestTag("Height") }
  val weight: KNode = child { hasTestTag("Weight") }
  val calorieGoal: KNode = child { hasTestTag("CalorieGoal") }
}

class AccountSettingsBottomBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<AccountSettingsBottomBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BottomBarRow") }) {

  val cancel: KNode = child { hasTestTag("Cancel") }
  val save: KNode = child { hasTestTag("Save") }
}
