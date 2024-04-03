package com.github.se.polyfit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.polyfit.data.local.dao.MealDao
import com.github.se.polyfit.data.local.entity.MealEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@Database(entities = [MealEntity::class], version = 1)
abstract class TestAppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
}

val testDatabase: TestAppDatabase = Room.inMemoryDatabaseBuilder(
    InstrumentationRegistry.getInstrumentation().context,
    TestAppDatabase::class.java
).allowMainThreadQueries().build()

@RunWith(AndroidJUnit4::class)
class MealDaoTest {

    private lateinit var mealDao: MealDao
    private lateinit var db: TestAppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TestAppDatabase::class.java).build()
        mealDao = db.mealDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetMeal() = runBlocking {
        val meal = MealEntity(1, "Breakfast", "Eggs", 98.6)
        mealDao.insert(meal)
        val byId = mealDao.getMeal(1)
        assertEquals(byId?.name, "Eggs")
    }
}