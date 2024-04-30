package com.github.se.polyfit.ui.components.selector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.ui.theme.PurpleGrey80

@Composable
fun PictureSelector(modifier: Modifier = Modifier) {
  // Not sure if we are going to use a picture for the post, so this is just a placeholder
  Box(
      modifier = modifier.fillMaxWidth().testTag("PictureSelector"),
      contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier.size(200.dp)
                    .background(color = PurpleGrey80, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center) {
              Text("Picture Placeholder")
            }
      }
}
