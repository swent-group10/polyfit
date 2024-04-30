package com.github.se.polyfit.ui.components.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.utils.titleCase

// TODO - Replace other Simple TopBars with this, used it in AdditionalMealInfoScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(title: String, navigateBack: () -> Unit) {
  TopAppBar(
      title = {
        Text(
            title,
            modifier = Modifier.testTag("${titleCase(title)} Title"),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize)
      },
      navigationIcon = {
        IconButton(
            onClick = { navigateBack() },
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
