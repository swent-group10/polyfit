package com.github.se.polyfit.di

import android.content.Context
import androidx.room.Room
import com.github.se.polyfit.data.api.OpenFoodFacts.OpenFoodFactsApi
import com.github.se.polyfit.data.api.Spoonacular.SpoonacularApiCaller
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.local.database.MealDatabase
import com.github.se.polyfit.data.processor.LocalDataProcessor
import com.github.se.polyfit.data.remote.firebase.MealFirebaseRepository
import com.github.se.polyfit.data.remote.firebase.PostFirebaseRepository
import com.github.se.polyfit.data.remote.firebase.UserFirebaseRepository
import com.github.se.polyfit.data.repository.MealRepository
import com.github.se.polyfit.model.data.User
import com.github.se.polyfit.model.post.PostLocationModel
import com.github.se.polyfit.viewmodel.graph.GraphViewModel
import com.github.se.polyfit.viewmodel.recipe.RecipeRecommendationViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltProvides {

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
  fun providesUserFirebaseRepository(): UserFirebaseRepository {
    return UserFirebaseRepository()
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
      user: User
  ): MealRepository {
    return MealRepository(context, mealFirebaseRepository, mealDao, user)
  }

  @Provides
  @Singleton
  fun providesLocalDataProcessor(mealDao: MealDao, user: User): LocalDataProcessor {
    return LocalDataProcessor(mealDao, user)
  }

  @Provides
  @Singleton
  fun providePostFirebaseRepository(): PostFirebaseRepository {
    return PostFirebaseRepository()
  }

  @Provides
  @Singleton
  fun providesLocationModel(@ApplicationContext context: Context): PostLocationModel {
    return PostLocationModel(context)
  }

  @Provides
  @Singleton
  fun providesFirebaseAuth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
  }

  @Provides
  @Singleton
  fun provideFoodFactsApi(): OpenFoodFactsApi {
    return OpenFoodFactsApi()
  }

  @Provides
  @Singleton
  fun providesRecipeRecommendationViewModel(
      spoonacularApiCaller: SpoonacularApiCaller
  ): RecipeRecommendationViewModel {
    return RecipeRecommendationViewModel(spoonacularApiCaller)
  }
}
