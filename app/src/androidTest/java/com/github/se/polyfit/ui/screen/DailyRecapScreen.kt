package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class DailyRecapScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<DailyRecapScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("DailyRecapScreen") }) {

  val spinner: KNode = child { hasTestTag("Spinner") }
}

class DailyRecapTopBar(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<DailyRecapTopBar>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("TopBar") }) {

  val title: KNode = child { hasTestTag("Overview Title") }
  val backButton: KNode = child { hasTestTag("BackButton") }
}
