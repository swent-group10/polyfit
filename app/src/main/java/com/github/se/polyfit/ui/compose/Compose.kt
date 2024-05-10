package com.github.se.polyfit.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PrimaryPurple

val gradiant_blue_pink = Brush.horizontalGradient(listOf(PrimaryPurple, PrimaryPink))

@Composable
fun Title(text: String) {
  val shape = RoundedCornerShape(35)

  Box(
      modifier =
          Modifier.clip(shape)
              .background(color = MaterialTheme.colorScheme.background)
              .padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = text,
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            modifier = Modifier.testTag("TopBarTitle"),
            fontWeight = FontWeight.Bold)
      }
}
