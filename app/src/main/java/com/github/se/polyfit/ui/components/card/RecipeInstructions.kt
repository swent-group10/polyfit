package com.github.se.polyfit.ui.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecipeInstructions(step: String, stepNumber: Int) {
  val commonModifier = Modifier.fillMaxWidth()
  Card(modifier = commonModifier.padding(8.dp), shape = RoundedCornerShape(8.dp)) {
    Column(modifier = commonModifier.fillMaxWidth().padding(16.dp)) {
      Text(
          text = "Step $stepNumber",
          fontWeight = FontWeight.Bold,
          fontSize = MaterialTheme.typography.bodyLarge.fontSize,
          modifier = Modifier.padding(bottom = 8.dp))
      Text(
          text = step,
          fontSize = MaterialTheme.typography.bodyMedium.fontSize,
          style = MaterialTheme.typography.displaySmall,
          lineHeight = 16.sp)
    }
  }
}
