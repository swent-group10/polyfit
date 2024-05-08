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
import com.github.se.polyfit.model.recipe.Recipe

@Composable
fun BookmarkButton(
    recipe: Recipe,
    addRecipe: (Recipe) -> Unit = {},
    removeRecipe: (Recipe) -> Unit = {},
    modifier: Modifier = Modifier,
) {
  var isClicked by remember { mutableStateOf(false) }
  IconToggleButton(
      checked = isClicked,
      onCheckedChange = {
        isClicked = !isClicked
        if (isClicked) {
          addRecipe(recipe)
        } else {
          removeRecipe(recipe)
        }
      },
      modifier = modifier,
      colors = IconButtonDefaults.filledIconToggleButtonColors(containerColor = Color(0xFFE1BEE7)),
  ) {
    Icon(
        imageVector = if (isClicked) Icons.Default.Done else Icons.Default.Add,
        contentDescription = "Bookmark Button",
        tint = Color.Red)
  }
}
