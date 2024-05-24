package com.github.se.polyfit.ui.components.card

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class TitleAndToggleCardScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<TitleAndToggleCardScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("TitleAndToggleCard") })
