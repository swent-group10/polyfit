package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class PostInfoScreenScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<PostInfoScreenScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("PostInfoScreen") })
