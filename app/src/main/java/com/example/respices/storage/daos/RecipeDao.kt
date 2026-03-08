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

// DAO managing the recipe table
@Dao
interface RecipeDao {
  // Basic CRUD

  // Insert a single Recipe
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(recipe: Recipe): Long

  // Delete a single Recipe
  @Delete
  suspend fun delete(recipe: Recipe)

  // Update a single Recipe
  @Update
  suspend fun update(recipe: Recipe)

  // Load all recipes
  @Query("SELECT * FROM recipes")
  suspend fun loadAll(): List<Recipe>

  // Load a single recipe by its id
  @Query("SELECT * FROM recipes WHERE recipeId = :id LIMIT 1")
  suspend fun loadById(id: Long): Recipe?

  // Delete all records in recipe table
  @Query("DELETE FROM recipes")
  suspend fun clearTable()

  // Meal

  // Load all meals
  @Transaction
  @Query("SELECT * FROM recipes")
  suspend fun loadAllMeals(): List<Meal>

  // Load a single meal by its id
  @Transaction
  @Query("SELECT * FROM recipes WHERE recipeId = :id LIMIT 1")
  suspend fun loadMealById(id: Long): Meal?
}