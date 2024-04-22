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
    // this is just a safegaured, the user should be signed in before this is called
    // if for some reason the user is not signed in, without this, it could cause and error
    // firestoredatabase needs a user id to be initialized
    return User(id = "testUserID")
  }

  @Provides
  @Singleton
  fun providesMealFirebaseRepository(user: User): MealFirebaseRepository {
    return MealFirebaseRepository(user.id)
  }

  @Provides
  @Singleton
  fun providesMealViewModel(mealFirebaseRepository: MealFirebaseRepository): MealViewModel {
    return MealViewModel(mealFirebaseRepository)
  }

  @Provides
  fun provideAuthentication(@ApplicationContext context: Context, user: User): AuthenticationCloud {
    return AuthenticationCloud(context, user)
  }
}
