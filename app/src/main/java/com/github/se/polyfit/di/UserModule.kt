package com.github.se.polyfit.di

import android.content.Context
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.utils.AuthenticationCloud
import com.github.se.polyfit.viewmodel.meal.MealViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

  @Provides
  @Singleton
  fun providesUser(): User {
    return User(id = "test")
  }

  @Provides
  @Singleton
  fun providesMealFirebaseRepository(user: User): MealFirebaseRepository {
    return MealFirebaseRepository(user.id)
  }

  @Provides
  @Singleton
  fun providesMealViewModel(
      user: User,
      mealFirebaseRepository: MealFirebaseRepository
  ): MealViewModel {
    return MealViewModel(user, mealFirebaseRepository)
  }

  @Provides
  fun provideAuthentication(@ApplicationContext context: Context, user: User): AuthenticationCloud {
    return AuthenticationCloud(context, user)
  }
}
