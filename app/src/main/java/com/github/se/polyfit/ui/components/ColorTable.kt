package com.github.se.polyfit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.meal.MealTagColor

const val COLORS_PER_ROW = 5

@Composable
fun ColorTable(
    colors: List<MealTagColor>,
    selectedColor: MealTagColor?,
    onColorSelected: (MealTagColor) -> Unit,
    modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    for (i in colors.indices step COLORS_PER_ROW) {
      Row(
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier.fillMaxWidth().testTag("ColorRow")) {
            for (j in i until i + COLORS_PER_ROW) {
              if (j < colors.size) {
                Surface(
                    color = colors[j].color,
                    shape = RoundedCornerShape(50.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    modifier =
                        Modifier.padding(6.dp, 2.dp)
                            .clickable { onColorSelected(colors[j]) }
                            .testTag("ColorButton")) {
                      Box(modifier = Modifier.height(32.dp).width(32.dp)) {
                        if (selectedColor == colors[j]) {
                          Icon(
                              imageVector = Icons.Default.Close,
                              contentDescription = "Selected",
                              tint = Color.Black,
                              modifier =
                                  Modifier.fillMaxWidth().fillMaxHeight().testTag("Selected"))
                        }
                      }
                    }
              }
            }
          }
    }
  }
}
