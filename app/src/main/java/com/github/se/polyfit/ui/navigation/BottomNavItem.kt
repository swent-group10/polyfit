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
  const val home = "homeNav"
  const val homeIcon = "homeIcon"
  const val homeLabel = "homeLabel"

  const val search = "searchNav"
  const val searchIcon = "searchIcon"
  const val searchLabel = "searchLabel"

  const val settings = "settingsNav"
  const val settingsIcon = "settingsIcon"
  const val settingsLabel = "settingsLabel"
}
