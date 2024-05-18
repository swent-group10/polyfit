package com.github.se.polyfit.ui.components.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.se.polyfit.ui.viewModel.SortDirection

@Composable
fun SortingArrows(modifier: Modifier = Modifier, onClick: () -> Unit, direction: SortDirection) {

  Box(modifier = modifier.clickable(onClick = onClick)) {
    when (direction) {
      SortDirection.ASCENDING ->
          Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "ArrowUp")
      SortDirection.DESCENDING ->
          Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "ArrowDown")
    }
  }
}
