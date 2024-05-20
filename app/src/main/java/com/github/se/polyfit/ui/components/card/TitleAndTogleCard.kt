package com.github.se.polyfit.ui.components.card

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel

@Composable
fun TitleAndToggleCard(
    title: String,
    button1Title: String,
    button2Title: String,
    recipeRecViewModel: RecipeRecommendationViewModel,
) {
  val showIngredient by recipeRecViewModel.showIngredient.observeAsState(initial = true)

  Card(
      modifier = Modifier.padding(16.dp).fillMaxWidth().testTag("TitleAndToggleCard"),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
          Text(
              text = title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(bottom = 16.dp).testTag("RecipeRecTitle"))
          Row(
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()) {
                FilledTonalButton(
                    onClick = {
                      Log.d("TitleAndToggleCard", "button1Title clicked")
                      Log.d("TitleAndToggleCard", "showIngredient: $showIngredient")
                      recipeRecViewModel.setShowIngredientTrue()
                    },
                    modifier = Modifier.weight(1f).testTag("tonalButton1"),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (showIngredient) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))) {
                      Text(
                          text = button1Title,
                          color =
                              if (showIngredient) Color.White
                              else MaterialTheme.colorScheme.primary)
                    }

                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = { recipeRecViewModel.setShowIngredientFalse() },
                    modifier = Modifier.weight(1f).testTag("tonalButton2"),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (!showIngredient) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))) {
                      Text(
                          text = button2Title,
                          color =
                              if (!showIngredient) Color.White
                              else MaterialTheme.colorScheme.primary)
                    }
              }
        }
      }
}
