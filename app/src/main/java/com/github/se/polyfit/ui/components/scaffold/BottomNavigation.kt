package com.github.se.polyfit.ui.components.scaffold

import android.Manifest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavBackStackEntry
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.dialog.launcherForActivityResult
import com.github.se.polyfit.ui.navigation.Route
import com.github.se.polyfit.ui.utils.OverviewTags

@Composable
fun BottomNavigationBar(
    backStackEntry: NavBackStackEntry?,
    navHome: () -> Unit,
    navPostInfo: () -> Unit,
    navSettings: () -> Unit,
    navMap: () -> Unit,
) {
  val context = LocalContext.current
  val launcher =
      launcherForActivityResult(
          onDeny = {}, onApproveForPost = {}, onApproveForMap = navMap, requestForMap = true)

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
        onClick = { if (currentRoute != Route.Home) navHome() })

    NavigationBarItem(
        modifier = Modifier.testTag(OverviewTags.overviewMapBtn),
        colors =
            NavigationBarItemDefaults.colors(
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedIconColor = MaterialTheme.colorScheme.primary),
        icon = {
          if (currentRoute != Route.Map) {
            Icon(Icons.Default.Place, contentDescription = OverviewTags.overviewMapIcon)
          } else {
            Icon(Icons.Default.Menu, contentDescription = OverviewTags.overviewPostsIcon)
          }
        },
        label = {
          if (currentRoute == Route.Map) {
            Text("Posts", Modifier.testTag(OverviewTags.overviewPostsLabel))
          } else if (currentRoute == Route.PostInfo) {
            Text("Map", Modifier.testTag(OverviewTags.overviewPostsLabel))
          }
        },
        selected = false,
        onClick = {
          if (currentRoute != Route.Map) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
          } else if (currentRoute == Route.Map) {
            navPostInfo()
          }
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
