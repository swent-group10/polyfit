package com.github.se.polyfit.ui.components

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class GradientBox(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<GradientBox>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("GradientBox") }) {}
