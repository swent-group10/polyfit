package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LoginScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("LoginScreen") }) {

  val loginColumn: KNode = child { hasTestTag("LoginColumn") }

  // Structural elements of the UI
  val loginTitle: KNode = loginColumn.child { hasTestTag("TopBarTitle") }
  val loginButton: KNode = loginColumn.child { hasTestTag("LoginButton") }
  val loginTerm: KNode = loginColumn.child { hasTestTag("LoginTerms") }
}
