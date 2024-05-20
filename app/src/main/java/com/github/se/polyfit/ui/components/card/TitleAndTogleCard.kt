package com.github.se.polyfit.ui.components.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.model.recipe.Recipe

@Composable
fun TitleAndToggleCard(
    title: String = "Title",
    button1Title: String = "Button1",
    button2Title: String = "Button2"
) {
  var isButton1Enabled by remember { mutableStateOf(true) }

  Card(
      modifier = Modifier.padding(16.dp).fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
          Text(
              text = title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(bottom = 16.dp))
          Row(
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()) {
                FilledTonalButton(
                    onClick = { isButton1Enabled = !isButton1Enabled },
                    modifier = Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (isButton1Enabled) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))) {
                      Text(
                          text = button1Title,
                          color =
                              if (isButton1Enabled) Color.White
                              else MaterialTheme.colorScheme.primary)
                    }
                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = { isButton1Enabled = !isButton1Enabled },
                    modifier = Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (!isButton1Enabled) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))) {
                      Text(
                          text = button2Title,
                          color =
                              if (!isButton1Enabled) Color.White
                              else MaterialTheme.colorScheme.primary)
                    }
              }
        }
      }
}

@Composable
@Preview
fun previewCard() {
  TitleAndToggleCard(Recipe.default().title)
}
