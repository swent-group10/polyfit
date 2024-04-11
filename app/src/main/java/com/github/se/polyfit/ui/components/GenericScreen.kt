package com.github.se.polyfit.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.github.se.polyfit.ui.screen.OverviewScreeen

@Composable
fun GenericScreen(
    navController: NavHostController,
) {
    val context: Context = LocalContext.current
    Scaffold(
        topBar = { AppTitle() },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                showToastMessage(context = context),
                showToastMessage(context = context),
                showToastMessage(context = context)
            )
        },
        content = { paddingValues -> OverviewScreeen(paddingValues) })
}

@Composable
fun showToastMessage(context: Context): () -> Unit {
    val toast = Toast.makeText(context, "not available yet", Toast.LENGTH_SHORT)

    return { toast.show() }
}
