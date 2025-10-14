package com.example.respices.storage.crossrefs

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag

@Entity(primaryKeys = ["recipeId", "tagId"],
  foreignKeys = [
    ForeignKey(
      entity = Recipe::class,
      parentColumns = ["recipeId"],
      childColumns = ["recipeId"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = Tag::class,
      parentColumns = ["tagId"],
      childColumns = ["tagId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("recipeId"), Index("tagId")])
data class RecipeTagCrossRef(
  val recipeId: Long,
  val tagId: Long
)
