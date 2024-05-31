# Package: com.github.se.polyfit.di

This package is responsible for managing dependency injection within the application using Dagger
Hilt. It contains modules that provide instances of various classes used throughout the application.
These instances are provided as singletons, meaning they are the same across the entire application.

## Key Components

1. **HiltProvides**: This module provides instances of various classes including User,
   GraphViewModel,
   SpoonacularApiCaller, MealFirebaseRepository, UserFirebaseRepository, MealDatabase, MealDao,
   MealRepository, LocalDataProcessor, PostFirebaseRepository, PostLocationModel, FirebaseAuth, and
   BarCodeCodeViewModel.

