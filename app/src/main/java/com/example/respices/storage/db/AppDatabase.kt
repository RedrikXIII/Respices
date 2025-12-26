package com.example.respices.storage.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.crossrefs.RecipeTagCrossRef
import com.example.respices.storage.daos.CrossRefDao
import com.example.respices.storage.daos.IngredientDao
import com.example.respices.storage.daos.RecipeDao
import com.example.respices.storage.daos.TagDao
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag

@Database(
  entities = [
    Recipe::class,
    Ingredient::class,
    Tag::class,
    RecipeIngredientCrossRef::class,
    RecipeTagCrossRef::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  // DAOs
  abstract fun recipeDao(): RecipeDao
  abstract fun ingredientDao(): IngredientDao
  abstract fun tagDao(): TagDao
  abstract fun crossRefDao(): CrossRefDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,

                "app_database"
              ).fallbackToDestructiveMigration(false).build()
        INSTANCE = instance
        instance
      }
    }
  }
}