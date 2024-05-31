package com.github.se.polyfit.ui.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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

// Arbitrary limit on the number of tags that can be added to a meal
const val MAX_TAGS = 5

/**
 * A list of meal tags that can be added to a meal
 *
 * @param mealTags the list of meal tags to display
 * @param addNewTag a function to add a new tag
 * @param editTag a function to edit a tag
 * @param modifier the modifier for the list
 * @param displayOnly whether the list is for display only
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MealTagList(
    mealTags: MutableList<MealTag>,
    addNewTag: () -> Unit,
    editTag: (MealTag) -> Unit,
    modifier: Modifier = Modifier,
    displayOnly: Boolean = false,
) {

  // When a tag is clicked, the user can edit the tag aslong as the list is not for display only
  // We prefer to remove .clickable instead of disabling it so that that outer clickable modifier
  // can still work
  fun tagModifier(tag: MealTag): Modifier {
    return if (!displayOnly) Modifier.clickable { editTag(tag) } else Modifier
  }

  FlowRow(
      horizontalArrangement = Arrangement.Start,
      verticalArrangement = Arrangement.Top,
      modifier = modifier.testTag("MealTagList")) {
        val shape = RoundedCornerShape(2.dp)
        for (tag in mealTags) {
          Surface(
              color = tag.tagColor.color,
              contentColor = PurpleGrey40,
              shape = shape,
              modifier = tagModifier(tag).padding(2.dp).testTag("MealTag"),
          ) {
            Text(
                text = tag.tagName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(6.dp, 2.dp))
          }
        }
        if (!displayOnly && mealTags.size < MAX_TAGS) {
          Surface(
              color = SecondaryGrey,
              contentColor = Color.White,
              shape = shape,
              modifier = Modifier.padding(2.dp).clickable(onClick = addNewTag).testTag("AddTag"),
          ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Tag",
                modifier = Modifier.height(24.dp))
          }
        }
      }
}
