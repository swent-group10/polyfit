package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.se.polyfit.ui.navigation.BottomNavItem

@Composable
fun MainBottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
  val backStackEntry = navController.currentBackStackEntryAsState()
  NavigationBar(
      modifier = modifier,
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      tonalElevation = 5.dp) {
        items.forEach { item ->
          val selected = item.route == backStackEntry.value?.destination?.route
          val selectedContentColor = MaterialTheme.colorScheme.inversePrimary
          val unselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
          NavigationBarItem(
              selected = selected,
              icon = {
                Column {
                  Icon(imageVector = item.icon, contentDescription = item.name)
                  if (selected) {
                    Text(
                        text = item.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center)
                  }
                }
              },
              onClick = { onItemClick(item) },
              colors =
                  NavigationBarItemDefaults.colors(
                      selectedIconColor = selectedContentColor,
                      unselectedIconColor = unselectedContentColor))
        }
      }
}
