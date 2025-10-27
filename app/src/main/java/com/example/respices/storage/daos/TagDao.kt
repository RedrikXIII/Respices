package com.example.respices.storage.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.respices.storage.entities.Tag

@Dao
interface TagDao {
  // Basic CRUD
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(tag: Tag): Long

  @Delete
  suspend fun delete(tag: Tag)

  @Update
  suspend fun update(tag: Tag)

  @Query("SELECT * FROM tags")
  suspend fun loadAll(): List<Tag>

  @Query("SELECT * FROM tags WHERE tagId = :id LIMIT 1")
  suspend fun loadById(id: Long): Tag?

  @Query("SELECT * FROM tags WHERE name LIKE :name LIMIT 1")
  suspend fun loadByName(name: String): Tag?

  @Query("DELETE FROM tags")
  suspend fun clearTable()
}