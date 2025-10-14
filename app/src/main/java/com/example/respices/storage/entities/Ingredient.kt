package com.example.respices.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
  @PrimaryKey(autoGenerate = true) val ingredientId: Long = 0L,

  val name: String
)
