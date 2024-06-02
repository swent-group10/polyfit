package com.github.se.polyfit.ui.components.button

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.ui.theme.PrimaryPurple

/**
 * Primary button with rounded corners and primary purple color by default
 *
 * @param modifier Modifier
 * @param onClick () -> Unit
 * @param text String
 * @param fontSize Int
 * @param isEnabled Boolean
 * @param color Color
 * @param buttonShape RoundedCornerShape
 * @param icon @Composable (() -> Unit)?
 */
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier.fillMaxWidth(0.5f),
    onClick: () -> Unit = {},
    text: String = "",
    fontSize: Int = 20,
    isEnabled: Boolean = true,
    color: Color = PrimaryPurple,
    buttonShape: RoundedCornerShape = RoundedCornerShape(50.dp),
    icon: @Composable (() -> Unit)? = null
) {
  var disabled by remember { mutableStateOf(false) }

  // Prevent Spam Clicking by enforcing that users wait 1 second after clicks
  LaunchedEffect(key1 = disabled) {
    if (disabled) {
      kotlinx.coroutines.delay(1000)
      disabled = false
    }
  }

  Button(
      onClick = {
        disabled = true
        onClick()
      },
      enabled = isEnabled && !disabled,
      modifier = modifier.testTag("PrimaryButton"),
      shape = buttonShape,
      colors = ButtonDefaults.buttonColors(containerColor = color)) {
        if (text.isNotBlank()) {
          Text(text, color = Color.White, fontSize = fontSize.sp, textAlign = TextAlign.Center)
        }
        icon?.invoke()
      }
}
