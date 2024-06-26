package com.github.se.polyfit.ui.components.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.theme.Purple80

@Composable
fun BookmarkButton(
    recipe: Recipe,
    addRecipe: (Recipe) -> Unit = {},
    removeRecipe: (Recipe) -> Unit = {},
    modifier: Modifier = Modifier,
) {
  var isChecked by remember { mutableStateOf(false) }
  IconToggleButton(
      checked = isChecked,
      onCheckedChange = {
        isChecked = !isChecked
        addOrRemoveRecipe(isChecked, recipe, addRecipe, removeRecipe)
      },
      modifier = modifier.testTag("BookmarkButton"),
      colors = IconButtonDefaults.filledIconToggleButtonColors(containerColor = Purple80),
  ) {
    Icon(
        imageVector = Icons.Default.Done.takeIf { isChecked } ?: Icons.Default.Add,
        contentDescription = "Bookmark Button",
        tint = Color.Red)
  }
}

private fun addOrRemoveRecipe(
    isChecked: Boolean,
    recipe: Recipe,
    addRecipe: (Recipe) -> Unit = {},
    removeRecipe: (Recipe) -> Unit = {}
) {
  when (isChecked) {
    true -> addRecipe(recipe)
    false -> removeRecipe(recipe)
  }
}
