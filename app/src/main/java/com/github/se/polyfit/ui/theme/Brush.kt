package com.github.se.polyfit.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

@Composable
fun getGradient(active: Boolean): Brush {
  val gradientActive =
      Brush.horizontalGradient(
          colors =
              listOf(MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.outlineVariant))
  val gradientInactive =
      Brush.horizontalGradient(colors = listOf(GradientButtonGrey, GradientButtonGrey))
  return if (active) gradientActive else gradientInactive
}
