package com.github.se.polyfit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.ui.theme.PurpleGrey40
import com.github.se.polyfit.ui.theme.SecondaryGrey

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MealTagList(mealTags: MutableList<MealTag>, addNewTag: () -> Unit, editTag: (MealTag) -> Unit) {
  FlowRow(
      horizontalArrangement = Arrangement.Start,
      verticalArrangement = Arrangement.Top,
      modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp).testTag("MealTagList")) {
        val shape = RoundedCornerShape(2.dp)

        for (tag in mealTags) {
          Surface(
              color = tag.tagColor.color,
              contentColor = PurpleGrey40,
              shape = shape,
              modifier =
                  Modifier.padding(2.dp, 4.dp)
                      .clickable(onClick = { editTag(tag) })
                      .testTag("MealTag"),
          ) {
            Text(
                text = tag.tagName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(6.dp, 2.dp))
          }
        }
        Surface(
            color = SecondaryGrey,
            contentColor = Color.White,
            shape = shape,
            modifier =
                Modifier.padding(2.dp, 4.dp).clickable(onClick = addNewTag).testTag("AddTag"),
        ) {
          Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add Tag",
              modifier = Modifier.height(24.dp))
        }
      }
}
