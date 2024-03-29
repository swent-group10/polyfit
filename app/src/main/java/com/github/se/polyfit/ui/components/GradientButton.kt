package com.github.se.polyfit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

/**
 * A button with a gradient border.
 *
 * @param onClick The callback to be invoked when this button is clicked.
 * @param active Whether the button is active or not. Inactive button is gray.
 * @param modifier The modifier to be applied to the button.
 * @param text The text to be displayed in the button.
 * @param icon The icon to be displayed in the button.
 * @param round Whether the button should be round or not (used when button is icon only).
 */
@Composable
fun GradientButton(
    onClick: () -> Unit,
    active: Boolean,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: @Composable (() -> Unit)? = null,
    round: Boolean = false,
) {
  val styles =
      mapOf(
          "gradient" to
              if (active) {
                Brush.horizontalGradient(colors = listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)))
              } else {
                Brush.horizontalGradient(colors = listOf(Color(0xFFE0E0E0), Color(0xFFE0E0E0)))
              },
          "textColor" to
              if (active) {
                MaterialTheme.colorScheme.primary
              } else {
                MaterialTheme.colorScheme.secondary
              },
          "rounding" to
              if (round) {
                RoundedCornerShape(50.dp)
              } else {
                RoundedCornerShape(20.dp)
              },
          "buttonModifier" to
              if (round) {
                Modifier.size(48.dp)
              } else {
                Modifier
              })

  Box(contentAlignment = Alignment.Center, modifier = modifier.padding(4.dp)) {
    Canvas(modifier = Modifier.matchParentSize()) {
      drawRoundRect(
          brush = styles["gradient"] as Brush,
          cornerRadius = CornerRadius(20.dp.toPx()),
          style = Stroke(2.dp.toPx()))
    }

    Button(
        onClick = onClick,
        shape = styles["rounding"] as RoundedCornerShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(12.dp),
        modifier = (styles["buttonModifier"] as Modifier).testTag("GradientButton")) {
          Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center) {
                if (icon != null) icon()
                if (text != null) {
                  Text(
                      text = text,
                      color = styles["textColor"] as Color,
                      fontSize = TextUnit(16f, TextUnitType.Sp),
                      fontWeight = FontWeight.Normal,
                      modifier = Modifier.testTag("ButtonText"))
                }
              }
        }
  }
}
