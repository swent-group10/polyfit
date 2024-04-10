package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.meal.Meal
import com.github.se.polyfit.ui.components.NutritionalInformation
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(meal: Meal, navigation: Navigation) {
  Scaffold(topBar = { TopBar(navigation = navigation) }, bottomBar = { BottomBar() }) { innerPadding
    ->
    Box(modifier = Modifier.padding(innerPadding)) { NutritionalInformation(meal = meal) }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(navigation: Navigation) {
  TopAppBar(
      title = {
        Text(
            "Nutrition Facts",
            modifier = Modifier.testTag("Title"),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium)
      },
      navigationIcon = {
        IconButton(
            onClick = { navigation.goBack() },
            content = {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  modifier = Modifier.testTag("BackButton"),
                  tint = PrimaryPurple)
            },
            modifier = Modifier.testTag("BackButton"))
      },
      modifier = Modifier.testTag("TopBar"))
}

@Composable
private fun BottomBar() {
  BottomAppBar(
      modifier = Modifier.height(128.dp).testTag("BottomBar"), containerColor = Color.Transparent) {
        Column(
            modifier = Modifier.fillMaxWidth().testTag("ButtonColumn"),
            horizontalAlignment = Alignment.CenterHorizontally) {
              Button(
                  onClick = { Log.v("Add Recipe", "Clicked") },
                  colors =
                      ButtonDefaults.buttonColors(
                          containerColor = PrimaryPink,
                          contentColor = MaterialTheme.colorScheme.onPrimary),
                  modifier = Modifier.width(250.dp).testTag("AddRecipeButton"),
              ) {
                Text(text = "Add Recipe", style = MaterialTheme.typography.bodyLarge)
              }
              Spacer(modifier = Modifier.height(8.dp))
              Button(
                  onClick = { Log.v("Add to Diary", "Clicked") },
                  colors =
                      ButtonDefaults.buttonColors(
                          containerColor = PrimaryPurple,
                          contentColor = MaterialTheme.colorScheme.onPrimary),
                  modifier = Modifier.width(250.dp).testTag("AddToDiaryButton"),
              ) {
                Text(text = "Add to Diary", style = MaterialTheme.typography.bodyLarge)
              }
            }
      }
}
