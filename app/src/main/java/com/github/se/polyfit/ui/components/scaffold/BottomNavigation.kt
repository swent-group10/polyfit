package com.github.se.polyfit.ui.components.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavBackStackEntry
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.utils.OverviewTags

@Composable
fun BottomNavigationBar(
    backStackEntry: NavBackStackEntry?,
    navHome: () -> Unit,
    navSearch: () -> Unit,
    navSettings: () -> Unit,
    navMap: () -> Unit,
) {
  val context = LocalContext.current
  var showingMap by remember { mutableStateOf(false) }

  NavigationBar(
      modifier = Modifier.testTag("MainBottomBar"),
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
  ) {
    val currentRoute = backStackEntry?.destination?.route

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
            Text(
                text = context.getString(R.string.home_nav_label),
                modifier = Modifier.testTag(OverviewTags.overviewHomeLabel))
          }
        },
        onClick = navHome)

    NavigationBarItem(
        modifier = Modifier.testTag(OverviewTags.overviewMapBtn),
        colors =
            NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary),
        icon = {
          Icon(
              if (showingMap) Icons.Default.Menu else Icons.Default.LocationOn,
              contentDescription = OverviewTags.overviewMapIcon)
        },
        label = {
          if (currentRoute == Route.PostInfo)
              Text(
                  if (showingMap) context.getString(R.string.map_nav_label)
                  else context.getString(R.string.map_nav_posts),
                  Modifier.testTag(OverviewTags.overviewMapLabel))
        },
        selected = currentRoute == Route.PostInfo,
        onClick = {
          showingMap = !showingMap
          navSearch.invoke()
        })

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
              Text(
                  context.getString(R.string.settings),
                  Modifier.testTag(OverviewTags.overviewSettingsLabel))
        },
        selected = currentRoute == Route.Settings,
        onClick = navSettings)
  }
}
