package com.github.se.polyfit.ui.components.button

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
import com.github.se.polyfit.ui.theme.GradientButtonGrey
import com.github.se.polyfit.ui.theme.GradientButtonPink
import com.github.se.polyfit.ui.theme.GradientButtonPurple

/**
 * A button with a gradient border. NOTE: For testing, see SelectIngredientsScreen.kt to see how to
 * properly select the click actions.
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
    textBold: Boolean = false,
) {
  val gradient =
      Brush.horizontalGradient(colors = listOf(GradientButtonPurple, GradientButtonPink)).takeIf {
        active
      } ?: Brush.horizontalGradient(colors = listOf(GradientButtonGrey, GradientButtonGrey))

  val textColor =
      MaterialTheme.colorScheme.primary.takeIf { active } ?: MaterialTheme.colorScheme.secondary

  val rounding = RoundedCornerShape(50.dp).takeIf { round } ?: RoundedCornerShape(20.dp)

  val buttonModifier = Modifier.size(48.dp).takeIf { round } ?: Modifier

  Box(contentAlignment = Alignment.Center, modifier = modifier.padding(4.dp)) {
    Canvas(modifier = Modifier.matchParentSize()) {
      drawRoundRect(
          brush = gradient, cornerRadius = CornerRadius(20.dp.toPx()), style = Stroke(2.dp.toPx()))
    }

    Button(
        onClick = onClick,
        shape = rounding,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(12.dp),
        modifier = buttonModifier.testTag("GradientButton")) {
          Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center) {
                icon?.invoke()
                text?.let {
                  Text(
                      text = text,
                      color = textColor,
                      fontSize = TextUnit(16f, TextUnitType.Sp),
                      fontWeight = if (textBold) FontWeight.ExtraBold else FontWeight.Normal,
                      modifier = Modifier.testTag("ButtonText"))
                }
              }
        }
  }
}
