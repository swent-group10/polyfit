package com.github.se.polyfit.ui.components.dialog

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class LocationPermissionScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LocationPermissionScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("LocationPermissionDialog") }) {

  val title: KNode = onNode { hasTestTag("Title") }
  val message: KNode = onNode { hasTestTag("Message") }
  val approveButton: KNode = onNode { hasTestTag("ApproveButton") }
  val denyButton: KNode = onNode { hasTestTag("DenyButton") }
}
