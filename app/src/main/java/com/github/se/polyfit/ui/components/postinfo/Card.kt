package com.github.se.polyfit.ui.components.postinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.se.polyfit.R
import com.github.se.polyfit.model.post.Post

@Composable
fun PostCard(post: Post) {
  val modifierPostCard =
      Modifier.fillMaxWidth().padding(8.dp).shadow(8.dp, shape = CardDefaults.shape)

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
