package com.github.se.polyfit.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.theme.BlueButton
import com.github.se.polyfit.ui.theme.PinkTitle


val kaiseiFont = FontFamily(
    Font(R.font.kaiseitokumin_bold, FontWeight.Bold),
    Font(R.font.kaiseitokumin_regular, FontWeight.Normal),
    Font(R.font.kaiseitokumin_extrabold, FontWeight.ExtraBold),
    Font(R.font.kaiseitokumin_medium, FontWeight.Medium)
)

val gradiant_blue_pink = Brush.horizontalGradient(listOf(BlueButton, PinkTitle))
@Composable
fun Titre(modifier: Modifier) {
  val shape = RoundedCornerShape(35)

  Box(modifier = Modifier
    .clip(shape)
    .background(color = MaterialTheme.colorScheme.background)
    .border(width = 2.dp,
      brush = gradiant_blue_pink,
      shape = shape)
    .padding(horizontal = 16.dp, vertical = 8.dp)
  ){

    Text(
      text = "PolyFit",
      fontSize = 50.sp,
      modifier = modifier,
      fontFamily = kaiseiFont,
      fontWeight = FontWeight.Bold,
    )
  }
}