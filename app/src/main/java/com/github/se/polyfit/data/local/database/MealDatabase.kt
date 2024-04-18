package com.github.se.polyfit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.local.entity.MealEntity
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.time.LocalDate

@Database(entities = [MealEntity::class], version = 1)
@TypeConverters(
    NutritionalInformationConverter::class, IngredientListConverter::class, TimeConverter::class)
abstract class MealDatabase : RoomDatabase() {
  abstract fun mealDao(): MealDao
}

class NutritionalInformationConverter {
  @TypeConverter
  fun fromNutritionalInformation(nutritionalInformation: NutritionalInformation): String {
    return Gson().toJson(nutritionalInformation)
  }

  @TypeConverter
  fun toNutritionalInformation(nutritionalInformationString: String): NutritionalInformation {
    return Gson().fromJson(nutritionalInformationString, NutritionalInformation::class.java)
  }
}

class IngredientListConverter {
  @TypeConverter
  fun fromIngredientList(ingredients: List<Ingredient>): String {
    return Gson().toJson(ingredients)
  }

  @TypeConverter
  fun toIngredientList(ingredientsString: String): List<Ingredient> {
    val type = object : TypeToken<List<Ingredient>>() {}.type
    return Gson().fromJson(ingredientsString, type)
  }
}

class TimeConverter {
  @TypeConverter
  fun fromTimestamp(value: Long?): LocalDate? {
    return value?.let { LocalDate.ofEpochDay(it) }
  }

  @TypeConverter
  fun dateToTimestamp(date: LocalDate?): Long? {
    return date?.toEpochDay()
  }
}
