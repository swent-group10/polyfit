package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LoginScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("LoginScreen") }) {

  // Structural elements of the UI
  val loginTitle: KNode = child { hasTestTag("TopBarTitle") }
  val loginButton: KNode = child { hasTestTag("LoginButton") }
  val loginTerm: KNode = child { hasTestTag("LoginTerms") }
}
