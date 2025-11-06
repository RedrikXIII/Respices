package com.example.respices.storage.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tags",
  indices = [
    Index(value = ["name"], unique = true)
  ]
)
data class Tag(
  @PrimaryKey(autoGenerate = true) val tagId: Long = 0L,

  var name: String
)
