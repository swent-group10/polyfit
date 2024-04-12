package com.github.se.polyfit.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.github.se.polyfit.ui.navigation.Route

@Composable
fun GenericScreen(navController: NavHostController, content: @Composable (PaddingValues) -> Unit) {
  val context: Context = LocalContext.current
  Scaffold(
      topBar = { AppTitle() },
      bottomBar = {
        BottomNavigationBar(
            navController = navController,
            { navController.navigate(Route.Home) },
            showToastMessage(context = context),
            showToastMessage(context = context))
      },
      content = content)
}

@Composable
fun showToastMessage(context: Context): () -> Unit {
  val toast = Toast.makeText(context, "not available yet", Toast.LENGTH_SHORT)

  return { toast.show() }
}
