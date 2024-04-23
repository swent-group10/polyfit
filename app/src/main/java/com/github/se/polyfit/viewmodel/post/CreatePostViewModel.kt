package com.github.se.polyfit.viewmodel.post

import androidx.lifecycle.ViewModel
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    mealRepository: MealRepository,
    postFirebaseRepository: MealFirebaseRepository,
) : ViewModel()
