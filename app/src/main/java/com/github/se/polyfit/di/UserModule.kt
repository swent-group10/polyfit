package com.github.se.polyfit.di

import android.content.Context
import androidx.room.Room
import com.github.se.polyfit.data.api.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.local.database.MealDatabase
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.ui.utils.Authentication
import com.github.se.polyfit.ui.viewModel.GraphViewModel
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

  fun providesGraphViewModel(dataProcessor: LocalDataProcessor): GraphViewModel {
    return GraphViewModel(dataProcessor)
  }

  @Provides
  @Singleton
  fun providesSpoonacularApiCaller(): SpoonacularApiCaller {
    return SpoonacularApiCaller()
  }

  @Provides
  @Singleton
  fun providesMealFirebaseRepository(user: User): MealFirebaseRepository {
    return MealFirebaseRepository(user.id)
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
  @Singleton
  fun providesLocalDataProcessor(mealDao: MealDao): LocalDataProcessor {
    return LocalDataProcessor(mealDao)
  }

  @Provides
  @Singleton
  fun providePostFirebaseRepository(): PostFirebaseRepository {
    return PostFirebaseRepository()
  }

  @Provides
  @Singleton
  fun provideAuthentication(@ApplicationContext context: Context, user: User): Authentication {
    return Authentication(context, user)
  }
}
