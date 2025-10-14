package com.example.respices.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
  @PrimaryKey(autoGenerate = true) val recipeId: Long = 0L,

  val name: String,
  val time: Long,
  val rating: Double,
  val steps: String,
  val link: String
)
