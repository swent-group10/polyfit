package com.github.se.polyfit.ui.components.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.utils.titleCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(title: String, titleColor: Color = PrimaryPurple, navigateBack: () -> Unit) {
  var isBackClicked by remember { mutableStateOf(false) }
  CenterAlignedTopAppBar(
      title = {
        Text(
            title,
            modifier = Modifier.testTag("${titleCase(title)} Title"),
            color = titleColor,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize)
      },
      navigationIcon = {
        IconButton(
            onClick = {
              if (!isBackClicked) {
                navigateBack()
                isBackClicked = true
              }
            },
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
