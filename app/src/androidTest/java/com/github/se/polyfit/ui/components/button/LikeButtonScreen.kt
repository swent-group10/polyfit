package com.github.se.polyfit.ui.components.button

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class LikeButtonScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LikeButtonScreen>(
        semanticsProvider = semanticsProvider, viewBuilderAction = { hasTestTag("LikeButton") })
