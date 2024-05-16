package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(items: List<String>, text: MutableState<String>, onItemSelect: (String) -> Unit) {
  val charSize = MaterialTheme.typography.titleMedium.fontSize.value.toInt()
  var estimatedWidth by remember { mutableStateOf(100) }
  val isExpanded = remember { mutableStateOf(false) }
  ExposedDropdownMenuBox(
      expanded = isExpanded.value,
      onExpandedChange = { isExpanded.value = it },
      modifier = Modifier.testTag("GraphDataSortingMenu").widthIn(max = estimatedWidth.dp)) {
        OutlinedTextField(
            value = text.value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded.value)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth().testTag("DropdownMenuTextField"),
            textStyle = MaterialTheme.typography.labelSmall,
            singleLine = true)

        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            modifier = Modifier.testTag("DropdownTab")) {
              items.forEach { t ->
                DropdownMenuItem(
                    text = { Text(t, maxLines = 1) },
                    onClick = {
                      text.value = t
                      isExpanded.value = false
                      onItemSelect(t)
                      val length = t.length + 2
                      estimatedWidth = (length * charSize).coerceIn(110, 200)
                    })
              }
            }
      }
}
