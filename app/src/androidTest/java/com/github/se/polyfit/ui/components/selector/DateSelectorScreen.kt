package com.github.se.polyfit.ui.components.selector

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class DateSelectorScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<DateSelectorScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("DateSelector") }) {

  val dateSelectorTitle: KNode = child { hasTestTag("Title") }
  private val dateBox: KNode = child { hasTestTag("DateBox") }
  val changeDateButton: KNode = dateBox.child { hasTestTag("ChangeDateButton") }
}
