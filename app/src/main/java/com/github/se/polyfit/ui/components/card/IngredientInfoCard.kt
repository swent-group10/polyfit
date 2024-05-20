package com.github.se.polyfit.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.se.polyfit.R
import com.github.se.polyfit.model.ingredient.Ingredient

@Composable
fun IngredientInfoCard(ingredient: Ingredient) {
  Card(
      onClick = {},
      modifier = Modifier.padding(16.dp).height(IntrinsicSize.Min).testTag("IngredientsCard")) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
              Row(
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                AsyncImage(
                    model =
                        ImageRequest.Builder(LocalContext.current)
                            .data(ingredient.imageUri)
                            .crossfade(true)
                            .build(),
                    placeholder = painterResource(R.drawable.logo),
                    error = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.description),
                    contentScale = ContentScale.Crop,
                    modifier =
                        Modifier.size(100.dp) // Ensures the image is a square
                            .testTag("IngredientsCard"))
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
              }
              Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = ingredient.amountFormated(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.align(Alignment.CenterEnd))
              }
            }
      }
}
