package com.github.se.polyfit.viewmodel.post

import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

@HiltViewModel
class ViewPostViewModel
@Inject
constructor(private val postFirebaseRepository: PostFirebaseRepository) : ViewModel() {

  fun getAllPost() = runBlocking { postFirebaseRepository.getAllPosts() }
}
