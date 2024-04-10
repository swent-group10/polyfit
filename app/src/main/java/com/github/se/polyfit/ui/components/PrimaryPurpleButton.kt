package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.ui.theme.PrimaryPurple

@Composable
fun PrimaryPurpleButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    text: String = "",
    isEnabled: Boolean = true
) {
  Button(
      onClick = onClick,
      enabled = isEnabled,
      modifier = modifier.fillMaxWidth(0.5f).testTag("PrimaryPurpleButton"),
      shape = RoundedCornerShape(50.dp),
      colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)) {
        Text(text, color = Color.White, fontSize = 20.sp)
      }
}
