package com.example.respices.storage.repositories

import android.content.Context
import androidx.room.Transaction
import com.example.respices.storage.crossrefs.RecipeIngredientCrossRef
import com.example.respices.storage.crossrefs.RecipeTagCrossRef
import com.example.respices.storage.daos.CrossRefDao
import com.example.respices.storage.daos.IngredientDao
import com.example.respices.storage.daos.RecipeDao
import com.example.respices.storage.daos.TagDao
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.support.services.LoggerService

class RecipeRepository(
  private val recipeDao: RecipeDao,
  private val ingredientDao: IngredientDao,
  private val tagDao: TagDao,
  private val crossRefDao: CrossRefDao,
  private val context: Context
) {
  @Transaction
  suspend fun insertMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>): Long {
    LoggerService.log("RecipeRepository: inserting a new meal...", context)

    val recipeId = recipeDao.insert(recipe)

    LoggerService.log("RecipeRepository: inserted a new recipe", context)

//    ingredients.forEach { ingredient ->
//      val existingIngredient = ingredientDao.loadByName(ingredient.name)
//      val ingredientId = existingIngredient?.ingredientId ?: ingredientDao.insert(ingredient)
//      crossRefDao.insert(RecipeIngredientCrossRef(recipeId, ingredientId))
//    }

    ingredients.forEach { ingredient ->
      val id = ingredientDao.insert(ingredient)
      val ingredientId = if (id == -1L) {
        ingredientDao.loadByName(ingredient.name)?.ingredientId ?: 0L
      } else id

      crossRefDao.insert(RecipeIngredientCrossRef(recipeId, ingredientId))
    }

    LoggerService.log("RecipeRepository: inserted all ingredients", context)

//    tags.forEach { tag ->
//      val existingTag = tagDao.loadByName(tag.name)
//      val tagId = existingTag?.tagId ?: tagDao.insert(tag)
//      crossRefDao.insert(RecipeTagCrossRef(recipeId, tagId))
//    }

    tags.forEach { tag ->
      val id = tagDao.insert(tag)
      val tagId = if (id == -1L) {
        tagDao.loadByName(tag.name)?.tagId ?: 0L
      } else id

      crossRefDao.insert(RecipeTagCrossRef(recipeId, tagId))
    }

    LoggerService.log("RecipeRepository: inserted all tags", context)

    return recipeId
  }

  @Transaction
  suspend fun deleteMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>) {
    LoggerService.log("RecipeRepository: deleting a meal...", context)

    recipeDao.delete(recipe)

    LoggerService.log("RecipeRepository: deleted a recipe", context)

    ingredients.forEach { ingredient ->
      if (crossRefDao.countIngredientCrossRefsById(ingredient.ingredientId) == 0L) {
        ingredientDao.delete(ingredient)
      }
    }

    LoggerService.log("RecipeRepository: deleted all single ingredients", context)

    tags.forEach { tag ->
      if (crossRefDao.countTagCrossRefsById(tag.tagId) == 0L) {
        tagDao.delete(tag)
      }
    }

    LoggerService.log("RecipeRepository: deleted all single tags", context)
  }

  suspend fun loadMealById(id: Long): Meal? {
    LoggerService.log("RecipeRepository: loading a meal by id...", context)
    val result = recipeDao.loadMealById(id)
    LoggerService.log("RecipeRepository: loaded a meal by id", context)
    return result
  }

  suspend fun loadAllMeals(): List<Meal> {
    LoggerService.log("RecipeRepository: loading all meals...", context)
    val result = recipeDao.loadAllMeals()
    LoggerService.log("RecipeRepository: loaded all meals", context)
    return result
  }

  suspend fun loadAllTags(): List<Tag> {
    LoggerService.log("RecipeRepository: loading all tags...", context)
    val result = tagDao.loadAll()
    LoggerService.log("RecipeRepository: loaded all tags", context)
    return result
  }

  suspend fun loadAllIngredients(): List<Ingredient> {
    LoggerService.log("RecipeRepository: loading all ingredients...", context)
    val result = ingredientDao.loadAll().toList()
    LoggerService.log("RecipeRepository: loaded all ingredients", context)
    return result
  }

  @Transaction
  suspend fun clearDatabase() {
    recipeDao.clearTable()
    tagDao.clearTable()
    ingredientDao.clearTable()
    crossRefDao.clearTableRI()
    crossRefDao.clearTableRT()
  }
}