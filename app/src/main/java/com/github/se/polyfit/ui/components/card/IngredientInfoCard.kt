package com.github.se.polyfit.ui.components.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.github.se.polyfit.data.api.Ingredient

@Composable
fun IngredientInfoCard(ingredient: Ingredient) {
  Card(
      modifier = Modifier.padding(16.dp).height(IntrinsicSize.Min).testTag("IngredientsCard"),
      shape = RoundedCornerShape(16.dp) // Adjust corner radius as needed
      ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth().height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
              AsyncImage(
                  model =
                      ImageRequest.Builder(LocalContext.current)
                          .data(ingredient.image)
                          .crossfade(true)
                          .build(),
                  placeholder = painterResource(R.drawable.logo),
                  error = painterResource(R.drawable.logo),
                  contentDescription = stringResource(R.string.description),
                  contentScale = ContentScale.Crop,
                  modifier =
                      Modifier.size(100.dp) // Ensures the image is a square
                          .clip(RoundedCornerShape(16.dp)) // Rounded corners for the image
                          .testTag("IngredientsCardImage"))
              Spacer(
                  modifier =
                      Modifier.width(16.dp)) // Spacer to add some space between the image and text
              Box(
                  modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                  contentAlignment = Alignment.CenterStart // Center the text in the remaining space
                  ) {
                    Text(
                        text = ingredient.name,
                        style =
                            MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                  }
            }
      }
}
