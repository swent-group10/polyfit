package com.github.se.polyfit.ui.components.IngredientsOverview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.button.PrimaryButton

@Composable
fun BottomBarIngredient(navigateForward: () -> Unit) {
  val context = LocalContext.current
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .testTag("GenerateBox")
              .background(MaterialTheme.colorScheme.surface)
              .padding(16.dp),
      contentAlignment = Alignment.Center) {
        PrimaryButton(
            text = ContextCompat.getString(context, R.string.GenerateRecipe),
            modifier = Modifier.align(Alignment.Center).testTag("GenerateButton"),
            onClick = navigateForward,
            color = MaterialTheme.colorScheme.outline)
      }
}
