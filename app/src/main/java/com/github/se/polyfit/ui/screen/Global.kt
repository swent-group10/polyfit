package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.github.se.polyfit.ui.navigation.BottomNavItem
import com.github.se.polyfit.ui.navigation.Route

@Composable
fun GenericScreens(navController: NavHostController, content : @Composable (PaddingValues) -> Unit, topBar : @Composable ()->Unit) {
  Scaffold(
      topBar = topBar,
      bottomBar = {
        BottomNavigationBar(
            items =
                listOf(
                    BottomNavItem(
                        name = "Overview", route = Route.Overview, icon = Icons.Default.Menu),
                    BottomNavItem(name = "Map", route = Route.Map, icon = Icons.Default.Place),
                    BottomNavItem(
                        name = "Settings", route = Route.Settings, icon = Icons.Default.Settings)),
            navController = navController,
            onItemClick = { navController.navigate(it.route) },
        )
      },
      content = content)
}


fun NavGraphBuilder.globalNavigation(navController: NavHostController) {
    navigation(startDestination = Route.Overview, route = Route.Home){
        composable(Route.Overview){
            GenericScreens(
                navController = navController,
                content = {
                    paddingValues -> OverviewContent(paddingValues)
                },
                topBar = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                        Title(modifier = Modifier, 35.sp)
                    }
                }
            )
        }
        composable(Route.Map){}
        composable(Route.Settings){}
    }
}

@Composable
fun BottomNavigationBar(
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
              colors = NavigationBarItemDefaults.colors(selectedIconColor = selectedContentColor, unselectedIconColor = unselectedContentColor))
        }
      }
}
