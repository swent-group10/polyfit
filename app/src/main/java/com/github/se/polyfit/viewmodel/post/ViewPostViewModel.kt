package com.github.se.polyfit.viewmodel.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ViewPostViewModel
@Inject
constructor(private val postFirebaseRepository: PostFirebaseRepository) : ViewModel() {
  private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(mutableListOf())
  val posts: StateFlow<List<Post>> = _posts

  private val _isFetching: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val isFetching: StateFlow<Boolean> = _isFetching

  init {
    getAllPost()
  }

  fun getAllPost() {
    viewModelScope.launch {
      _isFetching.value = true

      val newPosts =
          withContext(Dispatchers.Main) {
            postFirebaseRepository.getAllPosts().collect { posts -> _posts.value = posts }
          } // FYI: UI updates only on Main Thread

      _isFetching.value = false
    }
  }
}
