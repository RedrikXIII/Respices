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

@Dao
interface CrossRefDao {
  // RecipeIngredientCrossRef
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(ricr: RecipeIngredientCrossRef): Long

  @Delete
  suspend fun delete(ricr: RecipeIngredientCrossRef)

  @Update
  suspend fun update(ricr: RecipeIngredientCrossRef)

  @Query("SELECT COUNT(*) FROM RecipeIngredientCrossRef WHERE ingredientId = :id")
  suspend fun countIngredientCrossRefsById(id: Long): Long

  @Query("SELECT * FROM RecipeIngredientCrossRef WHERE recipeId = :recipeId")
  suspend fun loadAllRIByRecipe(recipeId: Long): List<RecipeIngredientCrossRef>

  @Query("DELETE FROM RecipeIngredientCrossRef")
  suspend fun clearTableRI()

  // RecipeTagCrossRef
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(rtcr: RecipeTagCrossRef): Long

  @Delete
  suspend fun delete(rtcr: RecipeTagCrossRef)

  @Update
  suspend fun update(rtcr: RecipeTagCrossRef)

  @Query("SELECT COUNT(*) FROM RecipeTagCrossRef WHERE tagId = :id")
  suspend fun countTagCrossRefsById(id: Long): Long

  @Query("SELECT * FROM RecipeTagCrossRef WHERE recipeId = :recipeId")
  suspend fun loadAllRTByRecipe(recipeId: Long): List<RecipeTagCrossRef>

  @Query("DELETE FROM RecipeTagCrossRef")
  suspend fun clearTableRT()
}