package com.github.se.polyfit.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.se.polyfit.R
import com.github.se.polyfit.ui.components.GenericScreen
import com.github.se.polyfit.ui.components.button.PrimaryButton
import com.github.se.polyfit.ui.components.postinfo.PostCard
import com.github.se.polyfit.ui.navigation.Navigation
import com.github.se.polyfit.viewmodel.post.ViewPostViewModel

@Composable
fun PostInfoScreen(
    navigation: Navigation,
    navHostController: NavHostController,
    viewPostViewModel: ViewPostViewModel = hiltViewModel(),
) {
  GenericScreen(
      navController = navHostController,
      content = { PostInfoScreenContent(viewPostViewModel = viewPostViewModel, padding = it) },
      modifier = Modifier.testTag("PostInfoScreen"),
      floatingButton = {
        PrimaryButton(
            text = "",
            icon = { Icon(Icons.Default.Create, contentDescription = "Create a Post") },
            onClick = navigation::navigateToCreatePost,
            buttonShape = RoundedCornerShape(100),
            modifier = Modifier.padding(top = 8.dp).testTag("CreateAPost"))
      })
}

@Composable
fun PostInfoScreenContent(
    index: Int = 0,
    padding: PaddingValues = PaddingValues(),
    viewPostViewModel: ViewPostViewModel = hiltViewModel(),
) {

  Scaffold(modifier = Modifier.padding(padding)) {
    val isFetching = viewPostViewModel.isFetching.value
    val posts = viewPostViewModel.posts.collectAsState(initial = emptyList()).value
    if (isFetching == true) {
      Log.d("PostInfoScreenContent", "Fetching")
      Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp).testTag("LoadingPost"))
      }
      return@Scaffold
    }
    if (posts.isEmpty()) {
      NoPost()
      return@Scaffold
    }

    LazyColumn(
        state = rememberLazyListState(index),
    ) {
      posts.forEach { post -> item { PostCard(post = post) } }
    }
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
