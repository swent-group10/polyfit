package com.github.se.polyfit.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton as FloatingActionButtonGeneric
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.theme.getGradient

@Composable
fun FloatingActionButtonIngredients(onClickFloatingButton: () -> Unit) {
  val shape = CircleShape
  FloatingActionButtonGeneric(
      modifier =
          Modifier.border(BorderStroke(2.dp, getGradient(true)), shape).testTag("FloatingButton"),
      onClick = onClickFloatingButton,
      containerColor = MaterialTheme.colorScheme.background,
      shape = shape,
      elevation = FloatingActionButtonDefaults.elevation(0.dp),
  ) {
    Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Add Ingredient",
        tint = MaterialTheme.colorScheme.outlineVariant,
        modifier = Modifier.size(32.dp).testTag("AddIcon"))
  }
}
