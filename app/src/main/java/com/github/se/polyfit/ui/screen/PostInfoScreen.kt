package com.github.se.polyfit.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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
import com.github.se.polyfit.R
import com.github.se.polyfit.model.post.Post
import com.github.se.polyfit.ui.components.postinfo.PostCard
import com.github.se.polyfit.viewmodel.post.ViewPostViewModel

@Composable
fun PostInfoScreen(
    posts: List<Post> = listOf(),
    index: Int = 0,
    viewPostViewModel: ViewPostViewModel = hiltViewModel(),
    navigateToCreatePost: () -> Unit
) {
    val posts by viewPostViewModel.posts.collectAsState(posts)
    val isFetching by viewPostViewModel.isFetching.collectAsState()

    Scaffold {

        if (isFetching) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
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
            items(posts) { post -> PostCard(post = post) }
        }
    }
}

@Composable
private fun NoPost() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = ContextCompat.getString(LocalContext.current, R.string.noPostAvailable),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.testTag("NoPostText")
        )
    }
}


