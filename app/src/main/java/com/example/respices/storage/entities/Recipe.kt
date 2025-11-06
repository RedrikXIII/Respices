package com.example.respices.storage.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
  @PrimaryKey(autoGenerate = true) val recipeId: Long = 0L,

  var name: String,
  var time: Long,
  var rating: Double,
  var steps: String,
  var link: String
)
