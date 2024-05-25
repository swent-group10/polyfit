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
fun recipeInstructions(step: String, stepNumber: Int) {
  Card(
      modifier = Modifier.fillMaxWidth().padding(8.dp),
      shape = RoundedCornerShape(8.dp),
      onClick = { /*TODO*/}) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
          Text(
              text = "Step $stepNumber",
              fontWeight = FontWeight.Bold,
              fontSize = MaterialTheme.typography.bodyLarge.fontSize,
              modifier = Modifier.padding(bottom = 8.dp))
          Text(
              text = step,
              fontSize = MaterialTheme.typography.bodyMedium.fontSize,
              style = MaterialTheme.typography.displaySmall,
              lineHeight = 16.sp // Adjust this value to your needs
              )
        }
      }
}
