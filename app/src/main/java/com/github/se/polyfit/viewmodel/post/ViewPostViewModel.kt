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
