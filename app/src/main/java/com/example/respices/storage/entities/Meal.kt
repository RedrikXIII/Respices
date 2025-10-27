package com.example.respices.storage.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.crossrefs.RecipeTagCrossRef

data class Meal(
  @Embedded val recipe: Recipe,

  @Relation(
    parentColumn = "recipeId",
    entityColumn = "ingredientId",
    associateBy = Junction(RecipeIngredientCrossRef::class)
  )
  val ingredients: List<Ingredient>,

  @Relation(
    parentColumn = "recipeId",
    entityColumn = "tagId",
    associateBy = Junction(RecipeTagCrossRef::class)
  )
  val tags: List<Tag>
)
