package com.example.respices.storage.repositories

import androidx.room.Transaction
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.crossrefs.RecipeTagCrossRef
import com.example.respices.storage.daos.CrossRefDao
import com.example.respices.storage.daos.IngredientDao
import com.example.respices.storage.daos.RecipeDao
import com.example.respices.storage.daos.TagDao
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.RecipeWithIngredientsAndTags
import com.example.respices.storage.entities.Tag

class RecipeRepository(
  private val recipeDao: RecipeDao,
  private val ingredientDao: IngredientDao,
  private val tagDao: TagDao,
  private val crossRefDao: CrossRefDao
) {
  @Transaction
  suspend fun insertRWIAT(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>): Long {
    val recipeId = recipeDao.insert(recipe)

    ingredients.forEach { ingredient ->
      val existingIngredient = ingredientDao.loadByName(ingredient.name)
      val ingredientId = existingIngredient?.ingredientId ?: ingredientDao.insert(ingredient)
      crossRefDao.insert(RecipeIngredientCrossRef(recipeId, ingredientId))
    }

    tags.forEach { tag ->
      val existingTag = tagDao.loadByName(tag.name)
      val tagId = existingTag?.tagId ?: tagDao.insert(tag)
      crossRefDao.insert(RecipeTagCrossRef(recipeId, tagId))
    }

    return recipeId
  }

  @Transaction
  suspend fun deleteRWIAT(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>) {
    val recipeId = recipe.recipeId
    recipeDao.delete(recipe)

    // Unreferenced Ingredients/Tags Cleanup
    ingredients.forEach { ingredient ->
      if (crossRefDao.countIngredientCrossRefsById(ingredient.ingredientId) == 0L) {
        ingredientDao.delete(ingredient)
      }
    }

    tags.forEach { tag ->
      if (crossRefDao.countTagCrossRefsById(tag.tagId) == 0L) {
        tagDao.delete(tag)
      }
    }
  }

  suspend fun loadRWIATById(id: Long): RecipeWithIngredientsAndTags? {
    return recipeDao.loadRWIATById(id)
  }

  suspend fun loadAllRWIATs(): List<RecipeWithIngredientsAndTags> {
    return recipeDao.loadAllRWIATs()
  }

  suspend fun loadAllTags(): List<Tag> {
    return tagDao.loadAll()
  }

  suspend fun loadAllIngredients(): List<Ingredient> {
    return ingredientDao.loadAll().toList()
  }
}