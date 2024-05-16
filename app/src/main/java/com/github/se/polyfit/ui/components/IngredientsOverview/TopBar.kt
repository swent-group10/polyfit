package com.github.se.polyfit.ui.components.IngredientsOverview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navigateBack: () -> Unit) {
  TopAppBar(
          title = {
            Text(
                    "Product",
                    modifier = Modifier.testTag("ProductTitle"),
                    color = MaterialTheme.colorScheme.onBackground,
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
                              tint = MaterialTheme.colorScheme.outline)
                    },
                    colors =
                    IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background),
                    modifier = Modifier.testTag("BackButton"))
          },
          modifier = Modifier.testTag("TopBar"),
          colors =
          TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background))
}