# Package: com.github.se.polyfit.di

This package is responsible for managing dependency injection within the application using Dagger
Hilt. It contains modules that provide instances of various classes used throughout the application.
These instances are provided as singletons, meaning they are the same across the entire application.

## Key Components

1. **UserModule**: This module provides instances of various classes including User, GraphViewModel,
   SpoonacularApiCaller, MealFirebaseRepository, UserFirebaseRepository, MealDatabase, MealDao,
   MealRepository, LocalDataProcessor, PostFirebaseRepository, PostLocationModel, FirebaseAuth, and
   BarCodeCodeViewModel.

## Key Classes

1. **User**: A safeguard to ensure a User instance is always available, even if the user is not
   signed in.
2. **GraphViewModel**: Used for processing and presenting data in a graphical format.
3. **SpoonacularApiCaller**: Used to make API calls to the Spoonacular service.
4. **MealFirebaseRepository**: Interacts with Firebase for operations related to meals.
5. **UserFirebaseRepository**: Interacts with Firebase for operations related to users.
6. **MealDatabase**: A Room database used for storing meal data locally.
7. **MealDao**: Interacts with the MealDatabase.
8. **MealRepository**: Interacts with both the MealFirebaseRepository and MealDao for operations
   related to meals.
9. **LocalDataProcessor**: Processes local data.
10. **PostFirebaseRepository**: Interacts with Firebase for operations related to posts.
11. **PostLocationModel**: Handles location data for posts.
12. **FirebaseAuth**: Used for authentication with Firebase.
13. **BarCodeCodeViewModel**: Handles barcode data.
