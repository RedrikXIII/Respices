package com.example.respices.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class Tag(
  @PrimaryKey(autoGenerate = true) val tagId: Long = 0L,

  val name: String
)
