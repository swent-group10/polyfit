package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.postinfo.PostCard
import com.github.se.polyfit.viewmodel.post.ViewPostViewModel

@Composable
fun PostInfoScreen(index: Int = 0, viewPostViewModel: ViewPostViewModel = hiltViewModel()) {
  val posts = viewPostViewModel.getAllPost()

  if (posts.isEmpty()) {
    NoPost()
    return
  }

  LazyColumn(
      state = rememberLazyListState(index),
  ) {
    items(posts) { post -> PostCard(post = post) }
  }
}

@Composable
private fun NoPost() {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Text(
        text = ContextCompat.getString(LocalContext.current, R.string.noPostAvailable),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.testTag("NoPostText"))
  }
}

/*
@Preview
@Composable
fun CarouselPreview() {
  val post = Post.default()
  post.meal.addIngredient(
      Ingredient(
          "ingredient1",
          100,
          100.0,
          MeasurementUnit.G,
          NutritionalInformation(
              mutableListOf(
                  Nutrient("Protein", 12.0, MeasurementUnit.G),
                  Nutrient("Carbohydrates", 21.50, MeasurementUnit.G),
                  Nutrient("Fats", 25.0, MeasurementUnit.G),
                  Nutrient("Vitamins", 40.0, MeasurementUnit.G),
              ))
      )
  )
  post.description = "Meal of the day, with a lot of nutrients and vitamins"
  val posts = listOf(post, post, post, post, post, post, post, post, post)

  PostInfoScreen(posts, 2)
}

@Preview
@Composable
fun NoPostPreview() {
  val posts = listOf<Post>()
  PostInfoScreen(posts)
}
*/
