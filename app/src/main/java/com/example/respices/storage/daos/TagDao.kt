package com.example.respices.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.respices.storage.entities.Tag

// DAO managing the tag table
@Dao
interface TagDao {
  // Basic CRUD

  // Insert a single Tag
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(tag: Tag): Long

  // Delete a single Tag
  @Delete
  suspend fun delete(tag: Tag)

  // Update a single Tag
  @Update
  suspend fun update(tag: Tag)

  // Load all tags
  @Query("SELECT * FROM tags")
  suspend fun loadAll(): List<Tag>

  // Load a single tag by its id
  @Query("SELECT * FROM tags WHERE tagId = :id LIMIT 1")
  suspend fun loadById(id: Long): Tag?

  // Load a single tag by its name
  @Query("SELECT * FROM tags WHERE name LIKE :name LIMIT 1")
  suspend fun loadByName(name: String): Tag?

  // Delete all records in tag table
  @Query("DELETE FROM tags")
  suspend fun clearTable()
}