package com.github.se.polyfit.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    isExpanded: MutableState<Boolean>,
    items: List<String>,
    text: MutableState<String>,
    onItemSelect: (String) -> Unit
) {
  ExposedDropdownMenuBox(
      expanded = isExpanded.value, onExpandedChange = { isExpanded.value = it }) {
        OutlinedTextField(
            value = text.value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
              ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded.value)
            },
            modifier = Modifier.menuAnchor().fillMaxWidth(0.5f),
            textStyle = MaterialTheme.typography.labelSmall)

        ExposedDropdownMenu(
            expanded = isExpanded.value, onDismissRequest = { isExpanded.value = false }) {
              items.forEach { t ->
                DropdownMenuItem(
                    text = { Text(t) },
                    onClick = {
                      text.value = t
                      isExpanded.value = false
                      onItemSelect(t)
                    })
              }
            }
      }
}
