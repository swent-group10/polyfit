package com.github.se.polyfit.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
  val context = LocalContext.current
  NavigationBar(
      modifier = Modifier.testTag("MainBottomBar"),
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
  ) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBarItem(
        icon = { Icon(Icons.Default.Home, contentDescription = null) },
        label = { Text("Home") },
        selected = currentRoute == "home",
        onClick = { showToastMessage(context) })

    NavigationBarItem(
        icon = { Icon(Icons.Default.Search, contentDescription = null) },
        label = { Text("Search") },
        selected = currentRoute == "search",
        onClick = { showToastMessage(context) })

    NavigationBarItem(
        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
        label = { Text("Settings") },
        selected = currentRoute == "settings",
        onClick = {
          // create a toast
          showToastMessage(context)
        })
  }
}

fun showToastMessage(context: Context) {
  val toast = Toast.makeText(context, "not available yet", Toast.LENGTH_SHORT)
  toast.show()
}
