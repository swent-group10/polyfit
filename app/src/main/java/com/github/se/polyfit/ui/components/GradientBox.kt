package com.github.se.polyfit.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.theme.*

val EmptyImageVector =
    ImageVector.Builder(
            defaultWidth = 0.dp, defaultHeight = 0.dp, viewportWidth = 0f, viewportHeight = 0f)
        .build()

// Gradient Box builds a white box with gradient border and an optional icon on the top right
@Composable
fun GradientBox(
    outerModifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    round: Double = 20.0, // Corner radius in dp
    borderSize: Double = 3.0, // Border thickness in dp
    gradientColors: List<Color> = listOf(PrimaryPurple, PrimaryPink), // Default gradient colors
    iconOnClick: () -> Unit,
    icon: ImageVector = EmptyImageVector,
    iconColor: Color = Color.Transparent,
    iconDescriptor: String = "",
    content: @Composable () -> Unit // Content lambda for the inner Box
) {
  Box(
      modifier =
          outerModifier
              .graphicsLayer {
                shape = RoundedCornerShape(round.dp)
                clip = true
              }
              .background(
                  brush =
                      Brush.linearGradient(
                          colors = gradientColors, start = Offset.Zero, end = Offset.Infinite))) {
        Box(
            modifier =
                innerModifier
                    .testTag("GradientBox")
                    .fillMaxWidth()
                    .padding(borderSize.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(round.dp))) {
              content()
            }

        IconButton(
            onClick = iconOnClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .testTag("TopRightIconInGradientBox")
        ) {
          Icon(icon, iconDescriptor, tint = iconColor)
        }
      }
}
