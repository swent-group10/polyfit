package com.github.se.polyfit.data.local.database

import android.net.Uri
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.local.entity.MealEntity
import com.github.se.polyfit.model.ingredient.Ingredient
import com.github.se.polyfit.model.meal.MealTag
import com.github.se.polyfit.model.nutritionalInformation.NutritionalInformation
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import java.time.LocalDate

@Database(
    entities = [MealEntity::class],
    version = 3,
    autoMigrations =
        [
            AutoMigration(from = 1, to = 2, spec = MealDatabase.RemoveMealId::class),
            AutoMigration(from = 2, to = 3, spec = MealDatabase.RemoveFirebaseId::class)])
@TypeConverters(
    NutritionalInformationConverter::class,
    IngredientListConverter::class,
    TimeConverter::class,
    TagListConverter::class,
    UriTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {
  abstract fun mealDao(): MealDao

  @DeleteColumn.Entries(DeleteColumn(tableName = "MealTable", columnName = "mealID"))
  class RemoveMealId : AutoMigrationSpec

  @DeleteColumn.Entries(DeleteColumn(tableName = "MealTable", columnName = "firebaseId"))
  class RemoveFirebaseId : AutoMigrationSpec
}

class NutritionalInformationConverter {
  private val gson = GsonBuilder().registerTypeAdapter(Uri::class.java, UriTypeAdapter()).create()

  @TypeConverter
  fun fromNutritionalInformation(nutritionalInformation: NutritionalInformation): String {
    return gson.toJson(nutritionalInformation)
  }

  @TypeConverter
  fun toNutritionalInformation(nutritionalInformationString: String): NutritionalInformation {
    return gson.fromJson(nutritionalInformationString, NutritionalInformation::class.java)
  }
}

class IngredientListConverter {
  private val gson = GsonBuilder().registerTypeAdapter(Uri::class.java, UriTypeAdapter()).create()

  @TypeConverter
  fun fromIngredientList(ingredients: List<Ingredient>): String {
    return gson.toJson(ingredients)
  }

  @TypeConverter
  fun toIngredientList(ingredientsString: String): List<Ingredient> {
    val type = object : TypeToken<List<Ingredient>>() {}.type
    return gson.fromJson(ingredientsString, type)
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

class UriTypeConverter {
  @TypeConverter
  fun fromUri(uri: Uri): String {
    return uri.toString()
  }

  @TypeConverter
  fun toUri(uriString: String): Uri {
    return Uri.parse(uriString)
  }
}

class TagListConverter {
  private val gson = GsonBuilder().registerTypeAdapter(Uri::class.java, UriTypeAdapter()).create()

  @TypeConverter
  fun fromTagList(tags: List<MealTag>): String {
    return gson.toJson(tags)
  }

  @TypeConverter
  fun toTagList(tagsString: String): List<MealTag> {
    val type = object : TypeToken<List<MealTag>>() {}.type
    return gson.fromJson(tagsString, type)
  }
}

class UriTypeAdapter : com.google.gson.TypeAdapter<Uri>() {
  override fun write(out: com.google.gson.stream.JsonWriter, value: Uri?) {
    out.value(value.toString())
  }

  override fun read(input: com.google.gson.stream.JsonReader): Uri {
    return Uri.parse(input.nextString())
  }
}
