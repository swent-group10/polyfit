package com.github.se.polyfit.viewmodel.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.model.post.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel for the post view. It is responsible for fetching the posts from the Firebase and
 * handling the user interaction with the posts. It is responsible for keeping the list up to date
 *
 * @property postFirebaseRepository PostFirebaseRepository
 */
@HiltViewModel
class ViewPostViewModel
@Inject
constructor(private val postFirebaseRepository: PostFirebaseRepository) : ViewModel() {
  private val _posts: MutableStateFlow<List<Post>> = MutableStateFlow(mutableListOf())

  private val _isFetching: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val isFetching: StateFlow<Boolean> = _isFetching

  init {
    postFirebaseRepository.initializeFirebaseListener(viewModelScope)
  }

  fun getAllPost(): LiveData<List<Post>> {
    return postFirebaseRepository.posts
  }
}
