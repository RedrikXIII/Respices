package com.example.respices.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.respices.storage.entities.Ingredient

@Dao
interface IngredientDao {
  // Basic CRUD
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(ingredient: Ingredient): Long

  @Delete
  suspend fun delete(ingredient: Ingredient)

  @Update
  suspend fun update(ingredient: Ingredient)

  @Query("SELECT * FROM ingredients")
  suspend fun loadAll(): Array<Ingredient>

  @Query("SELECT * FROM ingredients WHERE ingredientId = :id LIMIT 1")
  suspend fun loadById(id: Long): Ingredient?

  @Query("SELECT * FROM ingredients WHERE name LIKE :name LIMIT 1")
  suspend fun loadByName(name: String): Ingredient?
}