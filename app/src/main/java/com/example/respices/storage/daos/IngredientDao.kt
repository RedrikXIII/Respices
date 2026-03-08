package com.example.respices.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.entities.Ingredient

// DAO managing the ingredient table
@Dao
interface IngredientDao {
  // Basic CRUD

  // Insert a single Ingredient
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(ingredient: Ingredient): Long

  // Delete a single Ingredient
  @Delete
  suspend fun delete(ingredient: Ingredient)

  // Update a single Ingredient
  @Update
  suspend fun update(ingredient: Ingredient)

  // Load all ingredients
  @Query("SELECT * FROM ingredients")
  suspend fun loadAll(): Array<Ingredient>

  // Load a single ingredient by its id
  @Query("SELECT * FROM ingredients WHERE ingredientId = :id LIMIT 1")
  suspend fun loadById(id: Long): Ingredient?

  // Load a single ingredient by its name
  @Query("SELECT * FROM ingredients WHERE name LIKE :name LIMIT 1")
  suspend fun loadByName(name: String): Ingredient?

  // Delete all records in ingredient table
  @Query("DELETE FROM ingredients")
  suspend fun clearTable()
}