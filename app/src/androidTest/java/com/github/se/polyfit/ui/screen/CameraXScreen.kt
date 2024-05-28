package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CameraXScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CameraXScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BoxCamera") }) {

  // Top Bar
  val AndroidView: KNode = child { hasTestTag("AndroidView") }
}
