package com.github.se.polyfit.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.testTag
import com.github.se.polyfit.ui.navigation.Navigation

@Composable
fun HomeScreen(navigation: Navigation) {
  Text("Home Screen", modifier = androidx.compose.ui.Modifier.testTag("HomeScreenText"))
}
