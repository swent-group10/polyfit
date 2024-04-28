package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.github.se.polyfit.R
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.post.Post

@Composable
fun PostInfoScreen(posts: List<Post>, index: Int = 0) {

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
private fun PostCard(post: Post) {
  val modifierPostCard =
      Modifier.fillMaxWidth()
          .width(330.dp)
          .padding(8.dp)
          .shadow(8.dp, shape = CardDefaults.shape)
          .background(Color.Yellow)

  Card(modifier = modifierPostCard) {
    val modifier = Modifier.padding(8.dp, 3.dp)
    ImageCard(modifier)

    TextPostInfo(post = post, modifier = modifier)
  }
}

@Composable
private fun ImageCard(modifier: Modifier = Modifier) {
  Image(
      painter = painterResource(id = R.drawable.food1),
      contentDescription = null,
      contentScale = ContentScale.Fit,
      modifier = modifier.shadow(4.dp, shape = CardDefaults.shape))
}

@Composable
private fun TextPostInfo(modifier: Modifier = Modifier, post: Post) {

  Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
    TextPost(
        text = ContextCompat.getString(LocalContext.current, R.string.description),
        modifier = modifier.testTag("DescriptionTitle"),
        bold = true)

    TextPost(
        text = post.createdAt.dayOfMonth.toString() + " " + post.createdAt.month.toString(),
        modifier = modifier.testTag("Date"),
        bold = false)
  }

  TextPost(text = post.description, modifier = modifier.testTag("Description"), bold = false)

  TextPost(
      text = ContextCompat.getString(LocalContext.current, R.string.nutrient),
      modifier = modifier.testTag("NutrientTitle"),
      bold = true)

  post.meal.ingredients.forEach { it.nutritionalInformation.nutrients.forEach { NutrientInfo(it) } }
}

@Composable
private fun TextPost(text: String, modifier: Modifier = Modifier, bold: Boolean = false) {
  val style = MaterialTheme.typography.bodyMedium
  val fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal

  Text(
      text,
      modifier = modifier,
      color = MaterialTheme.colorScheme.onSurface,
      style = style,
      fontWeight = fontWeight)
}

@Composable
private fun NutrientInfo(
    nutrient: Nutrient,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
  Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth().padding(8.dp, 2.dp)) {
        Text(nutrient.getFormattedName(), style = style)
        Text(nutrient.getFormattedAmount(), style = style)
      }
}

@Composable
fun NoPost() {
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
              ))))
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
