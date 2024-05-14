package com.github.se.polyfit.ui.components.postinfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.se.polyfit.R
import com.github.se.polyfit.model.post.Post

@Composable
fun PostCard(post: Post) {
  val modifierPostCard =
      Modifier.fillMaxWidth().padding(8.dp).shadow(8.dp, shape = CardDefaults.shape)

  Card(modifier = modifierPostCard) {
    val modifier = Modifier.padding(8.dp, 3.dp)
    ImageCard(modifier, post)

    TextPostInfo(post = post, modifier = modifier)
  }
}

@Composable
private fun ImageCard(modifier: Modifier = Modifier, post: Post) {
  AsyncImage(
      model =
          ImageRequest.Builder(LocalContext.current)
              .data(post.imageDownloadURL)
              .crossfade(true)
              .build(),
      placeholder = painterResource(R.drawable.logo),
      contentDescription = stringResource(R.string.description),
      contentScale = ContentScale.Crop,
      modifier =
          modifier.shadow(4.dp, shape = CardDefaults.shape).fillMaxWidth().heightIn(max = 200.dp))
}
