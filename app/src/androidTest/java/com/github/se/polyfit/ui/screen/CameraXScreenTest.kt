package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class CameraXScreenTest(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<CameraXScreenTest>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("BoxCamera") }) {

  val cardCamera: KNode = child { hasTestTag("CardCamera") }
  val AndroidView: KNode = cardCamera.child { hasTestTag("AndroidView") }
}
