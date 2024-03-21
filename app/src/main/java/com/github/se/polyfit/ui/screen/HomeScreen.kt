package com.github.se.polyfit.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController

@Composable
fun HomeScreen() {
    Text("Home Screen",
        modifier = androidx.compose.ui.Modifier.testTag("HomeScreen") )
}