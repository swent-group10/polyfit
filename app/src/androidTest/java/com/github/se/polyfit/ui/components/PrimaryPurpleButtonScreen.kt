package com.github.se.polyfit.ui.components

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen

class PrimaryPurpleButtonScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<PrimaryPurpleButtonScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("PrimaryPurpleButton") }) {}
