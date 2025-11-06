package com.example.respices.storage.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "ingredients",
  indices = [
    Index(value = ["name"], unique = true)
  ]
)
data class Ingredient(
  @PrimaryKey(autoGenerate = true) val ingredientId: Long = 0L,

  var name: String
)
