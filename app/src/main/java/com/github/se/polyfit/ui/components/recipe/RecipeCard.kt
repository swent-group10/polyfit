package com.github.se.polyfit.ui.components.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.se.polyfit.R
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.button.LikeButton

@Composable
fun RecipeCard(recipe: Recipe) {
  Card(
      modifier =
          Modifier.height(200.dp)
              .padding(8.dp)
              .fillMaxSize() // Make the card fill the maximum available space
              .clip(RoundedCornerShape(16.dp)), // Apply this rounded shape to the card
      shape = RoundedCornerShape(16.dp), // Apply this rounded shape to the card
      elevation = CardDefaults.cardElevation(4.dp)) {
        Box(
            modifier = Modifier.fillMaxSize() // Make the box fill the entire card
            ) {
              // The image as the background
              AsyncImage(
                  model =
                      ImageRequest.Builder(LocalContext.current)
                          .data(recipe.imageUrl)
                          .crossfade(true)
                          .build(),
                  placeholder = painterResource(R.drawable.food1),
                  contentDescription = stringResource(R.string.description),
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize() // Make the image fill the entire box
                  )

              // Text positioned at the bottom-left
              Text(
                  text = recipe.title,
                  color = Color.White,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  modifier =
                      Modifier.align(Alignment.BottomStart)
                          .padding(horizontal = 8.dp, vertical = 4.dp))

              // Positioning LikeButton at the top-right
              LikeButton(
                  recipe.likes, modifier = Modifier.align(Alignment.TopEnd) // Add this line
                  )

              BookmarkButton(
                  isBookmarked = false,
                  onBookmarkClicked = { isBookmarked -> /* handle bookmark click */ },
                  modifier = Modifier.align(Alignment.BottomStart))
            }
      }
}

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
    onBookmarkClicked: (Boolean) -> Unit = {}
) {
  IconToggleButton(
      checked = isBookmarked, onCheckedChange = onBookmarkClicked, modifier = modifier) {
        Icon(
            imageVector = if (isBookmarked) Icons.Default.Star else Icons.Default.Star,
            contentDescription = "Bookmark Button",
            tint = Color.Red)
      }
}

@Preview
@Composable
fun preview() {
  RecipeCard(recipe = Recipe.default())
}
