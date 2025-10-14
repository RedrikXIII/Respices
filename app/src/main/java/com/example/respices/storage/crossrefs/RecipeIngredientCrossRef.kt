package com.example.respices.storage.crossrefs

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Recipe

@Entity(primaryKeys = ["recipeId", "ingredientId"],
  foreignKeys = [
    ForeignKey(
      entity = Recipe::class,
      parentColumns = ["recipeId"],
      childColumns = ["recipeId"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = Ingredient::class,
      parentColumns = ["ingredientId"],
      childColumns = ["ingredientId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("recipeId"), Index("ingredientId")])
data class RecipeIngredientCrossRef(
  val recipeId: Long,
  val ingredientId: Long
)
