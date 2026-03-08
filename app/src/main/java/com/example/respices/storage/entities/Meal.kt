package com.example.respices.storage.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.crossrefs.RecipeTagCrossRef

// Meal Entity
data class Meal(
  // Extend Recipe Entity
  @Embedded val recipe: Recipe,

  // Retrieve all assigned ingredients
  @Relation(
    parentColumn = "recipeId",
    entityColumn = "ingredientId",
    associateBy = Junction(RecipeIngredientCrossRef::class)
  )
  val ingredients: List<Ingredient>,

  // Retrieve all assigned tags
  @Relation(
    parentColumn = "recipeId",
    entityColumn = "tagId",
    associateBy = Junction(RecipeTagCrossRef::class)
  )
  val tags: List<Tag>
)
