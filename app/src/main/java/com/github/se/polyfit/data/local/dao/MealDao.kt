// package com.github.se.polyfit.data.local.dao
//
// import androidx.room.Dao
// import androidx.room.Insert
// import androidx.room.OnConflictStrategy
// import androidx.room.Query
// import com.github.se.polyfit.model.meal.Meal
//
// @Dao
// interface MealDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(meal: Meal)
//
//    @Query("SELECT * FROM meal WHERE mealID = :mealId")
//    suspend fun getMeal(mealId: Int): Meal?
//
//    @Query("SELECT * FROM meal")
//    suspend fun getAllMeals(): List<Meal>
// }
