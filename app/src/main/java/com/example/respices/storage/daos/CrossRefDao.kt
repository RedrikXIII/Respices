package com.example.respices.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.crossrefs.RecipeTagCrossRef

@Dao
interface CrossRefDao {
  // RecipeIngredientCrossRef
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(ricr: RecipeIngredientCrossRef): Long

  @Delete
  suspend fun delete(ricr: RecipeIngredientCrossRef)

  @Update
  suspend fun update(ricr: RecipeIngredientCrossRef)

  @Query("SELECT COUNT(*) FROM RecipeIngredientCrossRef WHERE ingredientId LIKE :id")
  suspend fun countIngredientCrossRefsById(id: Long): Long

  // RecipeTagCrossRef
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(rtcr: RecipeTagCrossRef): Long

  @Delete
  suspend fun delete(rtcr: RecipeTagCrossRef)

  @Update
  suspend fun update(rtcr: RecipeTagCrossRef)

  @Query("SELECT COUNT(*) FROM RecipeTagCrossRef WHERE tagId LIKE :id")
  suspend fun countTagCrossRefsById(id: Long): Long
}