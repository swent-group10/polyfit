package com.github.se.polyfit.ui.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.meal.MealTagColor
import com.github.se.polyfit.ui.components.ColorTable
import com.github.se.polyfit.ui.components.GradientBox
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.theme.PrimaryPink
import com.github.se.polyfit.ui.theme.PrimaryPurple
import com.github.se.polyfit.ui.theme.PurpleGrey40
import com.github.se.polyfit.ui.theme.SecondaryGrey

@Composable
fun MealTagDialog(
    tag: MealTag?,
    closeDialog: () -> Unit,
    addMealTag: (MealTag) -> Unit,
    removeMealTag: (MealTag) -> Unit
) {
  var name by remember(tag) { mutableStateOf(tag?.tagName ?: "") }
  var color by remember(tag) { mutableStateOf(tag?.tagColor ?: MealTagColor.UNDEFINED) }
  val allColors = MealTagColor.entries.filter { it != MealTagColor.UNDEFINED }

  fun onSave() {
    if (tag != null) {
      removeMealTag(tag)
    }

    val newTag = MealTag(name, color)
    addMealTag(newTag)
    closeDialog()
  }

  Dialog(onDismissRequest = closeDialog) {
    GradientBox(
        outerModifier = Modifier.testTag("AddMealTagDialog"),
        innerModifier = Modifier.fillMaxWidth(),
        iconOnClick = closeDialog,
        icon = Icons.Filled.Close,
        iconColor = PrimaryPink,
        iconDescriptor = "close",
    ) {
      Column(modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("MealTagContentContainer")) {
        Title(tag = tag, removeMealTag, closeDialog)

        EditLabel(name, onNameChange = { name = it })
        Spacer(modifier = Modifier.height(16.dp))

        EditColor(allColors, color, onColorSelected = { color = it })
        Spacer(modifier = Modifier.height(16.dp))

        SaveTag(name, color, ::onSave)
      }
    }
  }
}

@Composable
private fun SaveTag(
    name: String,
    color: MealTagColor,
    onSave: () -> Unit,
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
    PrimaryButton(
        text = "Save",
        isEnabled = name.isNotBlank() && color != MealTagColor.UNDEFINED,
        onClick = onSave,
        modifier = Modifier.testTag("SaveButton"))
  }
}

@Composable
private fun EditColor(
    allColors: List<MealTagColor>,
    color: MealTagColor,
    onColorSelected: (MealTagColor) -> Unit
) {
  Text(
      text = "Color",
      style = MaterialTheme.typography.labelLarge,
      color = PrimaryPurple,
      modifier = Modifier.testTag("Color"))
  Spacer(modifier = Modifier.height(8.dp))
  ColorTable(
      colors = allColors,
      selectedColor = color,
      onColorSelected = onColorSelected,
      modifier = Modifier.testTag("ColorTable"))
}

@Composable
private fun EditLabel(name: String, onNameChange: (String) -> Unit) {
  Text(
      text = "Label",
      style = MaterialTheme.typography.labelLarge,
      color = PrimaryPurple,
      modifier = Modifier.testTag("Label"))
  TextField(
      value = name,
      onValueChange = { onNameChange(it) },
      placeholder = { Text("Enter a label") },
      modifier = Modifier.testTag("LabelInput"),
      singleLine = true,
      colors =
          TextFieldDefaults.colors(
              focusedIndicatorColor = PrimaryPurple,
              unfocusedIndicatorColor = SecondaryGrey,
              focusedContainerColor = Color.Transparent,
              unfocusedContainerColor = Color.Transparent),
      keyboardOptions =
          KeyboardOptions(
              keyboardType = KeyboardType.Text,
              imeAction = ImeAction.Done,
          ))
}

@Composable
private fun Title(tag: MealTag?, removeMealTag: (MealTag) -> Unit, closeDialog: () -> Unit) {
  val title = if (tag == null) "Add a Tag" else "Edit Tag"

  Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp).testTag("TitleRow")) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = PurpleGrey40,
            modifier = Modifier.testTag("Title"))
        if (tag != null) {
          IconButton(
              onClick = {
                removeMealTag(tag)
                closeDialog()
              },
              modifier = Modifier.testTag("RemoveMealTagButton")) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Remove Tag",
                    tint = MaterialTheme.colorScheme.error)
              }
        }
      }
}
