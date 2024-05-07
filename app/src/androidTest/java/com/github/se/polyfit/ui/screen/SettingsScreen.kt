package com.github.se.polyfit.ui.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class SettingsScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<SettingsScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("SettingsScreen") }) {

  val title: KNode = child { hasTestTag("SettingsTitle") }
  private val settingList: KNode = child { hasTestTag("SettingsList") }
  private val settingItemColumn: KNode = settingList.child { hasTestTag("SettingItemColumn") }
  val settingItem: KNode = settingItemColumn.child { hasTestTag("SettingItem") }
  val divider: KNode = settingItemColumn.child { hasTestTag("Divider") }
}
