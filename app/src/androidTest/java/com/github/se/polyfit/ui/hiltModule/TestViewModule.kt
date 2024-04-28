package com.github.se.polyfit.ui.hiltModule

import com.github.se.polyfit.viewmodel.meal.MealViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk

@Module
@InstallIn(SingletonComponent::class)
object TestViewModelModule {
  @Provides
  fun provideMealViewModel(): MealViewModel = mockk(relaxed = true)
}