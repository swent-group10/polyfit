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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LikeButton(
    likesCount: Long,
    modifier: Modifier = Modifier,
    onLikeClicked: (Boolean) -> Unit = {}
) {
  FilledTonalButton(
      onClick = { onLikeClicked },
      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE1BEE7)),
      shape = RoundedCornerShape(20.dp),
      modifier = modifier.padding(horizontal = 25.dp, vertical = 8.dp)) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Like Button",
            tint = Color.Red,
            modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            text = likesCount.toString(),
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold)
      }
}

@Preview
@Composable
fun PreviewCustomLikeButton() {
  LikeButton(
      likesCount = 6113,
  )
}
