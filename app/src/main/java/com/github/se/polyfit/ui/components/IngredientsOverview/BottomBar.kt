package com.github.se.polyfit.ui.components.IngredientsOverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.components.button.PrimaryButton

@Composable
fun BottomBar(navigateForward: () -> Unit) {
  Box(
          modifier =
          Modifier.fillMaxWidth()
                  .testTag("DoneBox")
                  .background(MaterialTheme.colorScheme.surface)
                  .padding(16.dp),
          contentAlignment = Alignment.Center) {
    PrimaryButton(
            text = "Generate\nRecipe",
            modifier = Modifier.align(Alignment.Center),
            onClick = navigateForward,
            color = MaterialTheme.colorScheme.outline)
  }
}