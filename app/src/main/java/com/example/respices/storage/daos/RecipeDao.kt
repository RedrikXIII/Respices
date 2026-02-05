package com.example.respices.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Meal

// Recipe DAO
@Dao
interface RecipeDao {
  // Basic CRUD
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(recipe: Recipe): Long

  @Delete
  suspend fun delete(recipe: Recipe)

  @Update
  suspend fun update(recipe: Recipe)

  @Query("SELECT * FROM recipes")
  suspend fun loadAll(): List<Recipe>

  @Query("SELECT * FROM recipes WHERE recipeId = :id LIMIT 1")
  suspend fun loadById(id: Long): Recipe?

  // RecipeWithIngredientsAndTags
  @Transaction
  @Query("SELECT * FROM recipes")
  suspend fun loadAllMeals(): List<Meal>

  @Transaction
  @Query("SELECT * FROM recipes WHERE recipeId = :id LIMIT 1")
  suspend fun loadMealById(id: Long): Meal?

  @Query("DELETE FROM recipes")
  suspend fun clearTable()
}