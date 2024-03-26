package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.github.se.polyfit.ui.navigation.Navigation

@Composable
fun HomeScreen(navigation: Navigation) {
  Column {
    Text("Home Screen")
    Button(onClick = { navigation.navigateToIngredients() }) { Text("Go to Ingredients") }
  }
}
