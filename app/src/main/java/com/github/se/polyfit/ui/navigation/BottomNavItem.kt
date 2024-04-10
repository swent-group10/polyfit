package com.github.se.polyfit.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val itemTag: String,
    val iconTag: String
)

object NavItemTags {
  const val overviewItem = "overviewItem"
  const val overviewIcon = "overviewIcon"
  const val overviewName = "Overview"

  const val mapItem = "mapItem"
  const val mapIcon = "mapIcon"
  const val mapName = "Map"

  const val settingsItem = "settingsItem"
  const val settingsIcon = "settingsIcon"
  const val settingsName = "Settings"
}
