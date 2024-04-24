package com.github.se.polyfit.ui.components.button

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class PrimaryButtonScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<PrimaryButtonScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("PrimaryButton") }) {}
