package com.github.se.polyfit.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.utils.OverviewTags

@Composable
fun BottomNavigationBar(
    navController: NavController,
    navHome: () -> Unit,
    navSearch: () -> Unit,
    navSettings: () -> Unit,
) {
  val context = LocalContext.current
  NavigationBar(
      modifier = Modifier.testTag("MainBottomBar"),
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
  ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBarItem(
        modifier = Modifier.testTag(OverviewTags.overviewHomeBtn),
        colors =
            NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary),
        icon = { Icon(Icons.Default.Home, contentDescription = OverviewTags.overviewHomeIcon) },
        selected = currentRoute == Route.Home,
        label = {
          if (currentRoute == Route.Home) {
            Text(text = "Home", modifier = Modifier.testTag(OverviewTags.overviewHomeLabel))
          }
        },
        onClick = navHome)

    NavigationBarItem(
        modifier = Modifier.testTag(OverviewTags.overviewMapBtn),
        colors =
            NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary),
        icon = { Icon(Icons.Default.Search, contentDescription = OverviewTags.overviewMapIcon) },
        label = {
          if (currentRoute == Route.Map)
              Text("Search", Modifier.testTag(OverviewTags.overviewMapLabel))
        },
        selected = currentRoute == Route.Map,
        onClick = navSearch)

    NavigationBarItem(
        modifier = Modifier.testTag(OverviewTags.overviewSettingsBtn),
        colors =
            NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary),
        icon = {
          Icon(Icons.Default.Settings, contentDescription = OverviewTags.overviewSettingsIcon)
        },
        label = {
          if (currentRoute == Route.Settings)
              Text("Settings", Modifier.testTag(OverviewTags.overviewSettingsLabel))
        },
        selected = currentRoute == Route.Settings,
        onClick = navSettings)
  }
}
