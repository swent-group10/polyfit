package com.github.se.polyfit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.se.polyfit.data.local.entity.MealEntity

@Dao
interface MealDao {
  @Query("SELECT * FROM MealEntity") fun getAll(): List<MealEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(meal: MealEntity)

  @Query("DELETE FROM MealEntity") fun deleteAll()
}
