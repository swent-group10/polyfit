// package com.github.se.polyfit.ui.navigation
//
// import androidx.compose.material3.Text
// import androidx.navigation.NavGraphBuilder
// import androidx.navigation.NavHostController
// import androidx.navigation.compose.composable
// import androidx.navigation.navigation
// import com.github.se.polyfit.ui.components.GenericScreen
// import com.github.se.polyfit.ui.components.OverviewTopBar
// import com.github.se.polyfit.ui.screen.OverviewScreeen
//
//
// fun NavGraphBuilder.globalNavigation(navController: NavHostController) {
//    navigation(startDestination = Route.Overview, route = Route.Home) {
//        composable(Route.Overview) {
//            GenericScreen(
//                navController = navController,
//                content = { paddingValues -> OverviewScreeen(paddingValues) },
//                topBar = { OverviewTopBar() })
//        }
//        composable(Route.Map) {
//            GenericScreen(navController = navController, content = { Text("Map Screen") })
//        }
//        composable(Route.Settings) {
//            GenericScreen(navController = navController, content = { Text("Settings Screen") })
//        }
//    }
// }
