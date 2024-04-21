package com.github.se.polyfit.di

import android.content.Context
import androidx.room.Room
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.local.database.MealDatabase
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
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
  fun providesMealViewModel(mealRepo: MealRepository): MealViewModel {
    return MealViewModel(mealRepo)
  }

  @Provides
  @Singleton
  fun providesLocalDatabase(@ApplicationContext context: Context): MealDatabase {
    return Room.databaseBuilder(context, MealDatabase::class.java, "meal_database").build()
  }

  @Provides
  @Singleton
  fun providesMealDao(mealDatabase: MealDatabase): MealDao {
    return mealDatabase.mealDao()
  }

  @Provides
  @Singleton
  fun providesMealRepository(
      @ApplicationContext context: Context,
      mealFirebaseRepository: MealFirebaseRepository,
      mealDao: MealDao,
  ): MealRepository {
    return MealRepository(context, mealFirebaseRepository, mealDao)
  }

  @Provides
  fun provideAuthentication(@ApplicationContext context: Context, user: User): AuthenticationCloud {
    return AuthenticationCloud(context, user)
  }
}
