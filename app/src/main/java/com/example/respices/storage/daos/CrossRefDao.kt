package com.example.respices.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.crossrefs.RecipeTagCrossRef

// DAO managing both cross-reference tables
@Dao
interface CrossRefDao {
  // RecipeIngredientCrossRef

  // Insert a single RecipeIngredientCrossRef
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(ricr: RecipeIngredientCrossRef): Long

  // Delete a single RecipeIngredientCrossRef
  @Delete
  suspend fun delete(ricr: RecipeIngredientCrossRef)

  // Update a single RecipeIngredientCrossRef
  @Update
  suspend fun update(ricr: RecipeIngredientCrossRef)

  // Count how many times a certain Ingredient appears across all Recipes
  @Query("SELECT COUNT(*) FROM RecipeIngredientCrossRef WHERE ingredientId = :id")
  suspend fun countIngredientCrossRefsById(id: Long): Long

  // Load all RecipeIngredientCrossRef for a specific Recipe
  @Query("SELECT * FROM RecipeIngredientCrossRef WHERE recipeId = :recipeId")
  suspend fun loadAllRIByRecipe(recipeId: Long): List<RecipeIngredientCrossRef>

  // Delete all records in RecipeIngredientCrossRef
  @Query("DELETE FROM RecipeIngredientCrossRef")
  suspend fun clearTableRI()

  // RecipeTagCrossRef

  // Insert a single RecipeTagCrossRef
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(rtcr: RecipeTagCrossRef): Long

  // Delete a single RecipeTagCrossRef
  @Delete
  suspend fun delete(rtcr: RecipeTagCrossRef)

  // Update a single RecipeTagCrossRef
  @Update
  suspend fun update(rtcr: RecipeTagCrossRef)

  // Count how many times a certain Tag appears across all Recipes
  @Query("SELECT COUNT(*) FROM RecipeTagCrossRef WHERE tagId = :id")
  suspend fun countTagCrossRefsById(id: Long): Long

  // Load all RecipeTagCrossRef for a specific Recipe
  @Query("SELECT * FROM RecipeTagCrossRef WHERE recipeId = :recipeId")
  suspend fun loadAllRTByRecipe(recipeId: Long): List<RecipeTagCrossRef>

  // Delete all records in RecipeTagCrossRef
  @Query("DELETE FROM RecipeTagCrossRef")
  suspend fun clearTableRT()
}