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

// Recipe Repository
class RecipeRepository(
  private val recipeDao: RecipeDao,
  private val ingredientDao: IngredientDao,
  private val tagDao: TagDao,
  private val crossRefDao: CrossRefDao,
  private val context: Context
) {
  @Transaction
  suspend fun insertMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>): Long {
    val recipeId = recipeDao.insert(recipe)

    ingredients.forEach { ingredientI ->
      val ingredient = ingredientI.copy(name = ingredientI.name.lowercase())

      val id = ingredientDao.insert(ingredient)
      val ingredientId = if (id == -1L) {
        ingredientDao.loadByName(ingredient.name)?.ingredientId ?: 0L
      } else id

      if (ingredientId != 0L) {
        crossRefDao.insert(RecipeIngredientCrossRef(recipeId, ingredientId))
      }
    }

    tags.forEach { tagI ->
      val tag = tagI.copy(name = tagI.name.lowercase())

      val id = tagDao.insert(tag)
      val tagId = if (id == -1L) {
        tagDao.loadByName(tag.name)?.tagId ?: 0L
      } else id

      if (tagId != 0L) {
        crossRefDao.insert(RecipeTagCrossRef(recipeId, tagId))
      }
    }

    return recipeId
  }

  @Transaction
  suspend fun deleteMeal(recipe: Recipe) {
    val ingredientIds: List<Long> = crossRefDao.loadAllRIByRecipe(recipe.recipeId)
      .map { it.ingredientId }
    val tagIds: List<Long> = crossRefDao.loadAllRTByRecipe(recipe.recipeId)
      .map { it.tagId }

    recipeDao.delete(recipe)

    ingredientIds.forEach { ingredientId ->
      ingredientDao.loadById(ingredientId)?.let { ingredient ->
        if (crossRefDao.countIngredientCrossRefsById(ingredientId) == 0L) {
          ingredientDao.delete(ingredient)
        }
      }
    }

    tagIds.forEach { tagId ->
      tagDao.loadById(tagId)?.let { tag ->
        if (crossRefDao.countTagCrossRefsById(tagId) == 0L) {
          tagDao.delete(tag)
        }
      }
    }
  }

  suspend fun loadMealById(id: Long): Meal? {
    val result = recipeDao.loadMealById(id)
    return result
  }

  suspend fun loadAllMeals(): List<Meal> {
    val result = recipeDao.loadAllMeals()
    return result
  }

  suspend fun loadAllTags(): List<Tag> {
    val result = tagDao.loadAll()
    return result
  }

  suspend fun loadAllIngredients(): List<Ingredient> {
    val result = ingredientDao.loadAll().toList()
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