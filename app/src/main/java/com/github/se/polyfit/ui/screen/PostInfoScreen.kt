package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.R
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.MeasurementUnit
import com.github.se.polyfit.model.nutritionalInformation.Nutrient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.github.se.polyfit.model.post.Post

val lipsum = "\n" +
    "What is Lorem Ipsum?\n" +
    "\n" +
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
    "Why do we use it?\n" +
    "\n" +
    "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n" +
    "\n" +
    "Where does it come from?\n" +
    "\n" +
    "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32."

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel(posts: List<Post>, index: Int = 0) {
  // List of image resources

  // Temporary adding image to posts
  val rowState = rememberLazyListState(index)
  val flingBehavior = rememberSnapFlingBehavior(lazyListState = rowState)

  LazyRow(modifier = Modifier
    .fillMaxHeight(),
    horizontalArrangement = Arrangement.SpaceBetween,
    flingBehavior = flingBehavior,
    state = rowState,
  ) {
    items(posts) { post ->
      Card(modifier = Modifier
        .fillMaxHeight()
        .width(330.dp)
        .padding(8.dp)
        .verticalScroll(rememberScrollState())
        .background(MaterialTheme.colorScheme.surface)
      ) {

        Image(
          painter = painterResource(id = R.drawable.food1),
          contentDescription = null,
          contentScale = ContentScale.Fit,
          modifier = Modifier
            .padding(8.dp)
            .clip(CardDefaults.shape)
        )

        val modifier = Modifier.padding(8.dp)

        Text(text = "Description",
          fontWeight = FontWeight.Bold,
          modifier = modifier)

        Text(
          post.description,
          modifier = modifier,
          color = MaterialTheme.colorScheme.onSurface,
        )

        //Log.i("Carousel", "a, la valeur a ${a.second}")
        Text(
          "Nutrient",
          modifier = modifier,
          color = MaterialTheme.colorScheme.onSurface,
          fontWeight = FontWeight.Bold,
        )
        post.meal.ingredients.forEach {

          it.nutritionalInformation.nutrients.forEach {
            NutrientInfo(it)
          }
        }
      }
    }
  }
}

@Composable
private fun NutrientInfo(
  nutrient: Nutrient,
  style: TextStyle = MaterialTheme.typography.bodyMedium
) {
  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier.fillMaxWidth().padding(8.dp, 4.dp)) {
    Text(
      nutrient.getFormattedName(), style = style)
    Text(
      nutrient.getFormattedAmount(),
      style = style)
  }
}


@Preview
@Composable
fun CarouselPreview() {
  val post = Post.default()
  post.meal.addIngredient(Ingredient("ingredient1", 100, 100.0, MeasurementUnit.G,
    NutritionalInformation(mutableListOf(
      Nutrient("Protein", 12.0, MeasurementUnit.G),
      Nutrient("Carbohydrates", 21.50, MeasurementUnit.G),
      Nutrient("Fats", 25.0, MeasurementUnit.G),
      Nutrient("Vitamins", 40.0, MeasurementUnit.G),
      ))
  ));
  post.description = "Meal of the day, with a lot of nutrients and vitamins"
  val posts = listOf(post, post, post, post, post, post, post, post, post)

  Carousel(posts, 2)
}