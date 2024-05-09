package com.github.se.polyfit.ui.components.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LikeButton(
    likeCount: Long,
    modifier: Modifier = Modifier,
    onLikeClicked: (Boolean) -> Unit = {}
) {
  FilledTonalButton(
      onClick = { onLikeClicked },
      colors = ButtonDefaults.buttonColors(containerColor = Color.White),
      shape = RoundedCornerShape(20.dp),
      enabled = false, // Set this to true
      modifier = modifier.padding(horizontal = 25.dp, vertical = 8.dp).testTag("LikeButton")) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Like Button",
            tint = Color.Red,
            modifier = Modifier.size(24.dp).testTag("LikeIcon"))
        Spacer(Modifier.width(4.dp))
        Text(
            text = likeCount.toString(),
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag("LikeCount"))
      }
}
