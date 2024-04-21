package com.github.se.polyfit.ui.components.selector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.ui.components.MealTagList
import com.github.se.polyfit.ui.components.dialog.MealTagDialog
import com.github.se.polyfit.ui.theme.PurpleGrey40

@Composable
fun MealTagSelector(
    mealTags: MutableList<MealTag>,
    addMealTag: (MealTag) -> Unit,
    removeMealTag: (MealTag) -> Unit,
    modifier: Modifier = Modifier
) {
  var showTagDialog by remember { mutableStateOf(false) }
  var currentTag by remember { mutableStateOf<MealTag?>(null) }

  fun addNewTag() {
    showTagDialog = true
  }

  fun editTag(tag: MealTag) {
    currentTag = tag
    showTagDialog = true
  }

  fun closeDialog() {
    currentTag = null
    showTagDialog = false
  }

  Column(modifier = modifier.testTag("MealTagSelector")) {
    Text(
        text = "Special Tags",
        style = MaterialTheme.typography.titleLarge,
        color = PurpleGrey40,
        modifier = Modifier.padding(16.dp, 0.dp).testTag("Title"))

    Box(
        modifier = Modifier.fillMaxWidth().testTag("MealTagBox"),
        contentAlignment = Alignment.Center) {
          MealTagList(mealTags, ::addNewTag, ::editTag)
          if (showTagDialog) {
            MealTagDialog(currentTag, ::closeDialog, addMealTag, removeMealTag)
          }
        }
  }
}

@Preview
@Composable
fun MealTagSelectorPreview() {
  MealTagSelector(mutableListOf(), {}, {})
}
