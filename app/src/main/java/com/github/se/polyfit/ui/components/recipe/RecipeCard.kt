package com.github.se.polyfit.ui.components.recipe

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.github.se.polyfit.model.recipe.Recipe
import com.github.se.polyfit.ui.components.button.BookmarkButton
import com.github.se.polyfit.ui.components.button.LikeButton

@Composable
fun RecipeCard(
    recipe: Recipe,
    onCardClick: (Recipe) -> Unit = {},
    onBookmarkClick: (Recipe) -> Unit = {},
    onBookmarkRemove: (Recipe) -> Unit = {},
    showTitle: Boolean = true,
    showBookmark: Boolean = true
) {

  Card(
      modifier =
          Modifier.height(200.dp)
              .padding(8.dp)
              .fillMaxSize()
              .clip(RoundedCornerShape(16.dp))
              .testTag("RecipeCard"),
      shape = RoundedCornerShape(16.dp),
      elevation = CardDefaults.cardElevation(4.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
          RecipeImage(recipe, onCardClick)
          if (showTitle) {
            Text(
                text = recipe.title,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier =
                    Modifier.align(Alignment.BottomStart)
                        .padding(horizontal = 8.dp, vertical = 4.dp))
          }
          LikeButton(recipe.likes, modifier = Modifier.align(Alignment.TopEnd))

          if (showBookmark) {

            BookmarkButton(
                recipe, onBookmarkClick, onBookmarkRemove, Modifier.align(Alignment.BottomEnd))
          }
        }
      }
}

@Composable
fun RecipeImage(recipe: Recipe, onCardClick: (Recipe) -> Unit) {
  AsyncImage(
      model =
          ImageRequest.Builder(LocalContext.current)
              .data(recipe.imageUrl.toString())
              .crossfade(true)
              .build(),
      placeholder = painterResource(R.drawable.logo),
      contentDescription = stringResource(R.string.description),
      contentScale = ContentScale.Crop,
      modifier =
          Modifier.fillMaxSize()
              .clickable {
                Log.d("RecipeCard", "RecipeImage: ${recipe.title}")
                onCardClick(recipe)
              }
              .testTag("RecipeImage"))
}
