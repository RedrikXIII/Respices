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

// Recipe Repository
class RecipeRepository(
  private val recipeDao: RecipeDao,
  private val ingredientDao: IngredientDao,
  private val tagDao: TagDao,
  private val crossRefDao: CrossRefDao,
  private val context: Context
) {
  // Insert a single meal, with its assigned ingredients and tags
  @Transaction
  suspend fun insertMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>): Long {
    val recipeId = recipeDao.insert(recipe)

    // Checking whether any new ingredients need to be inserted
    ingredients.forEach { ingredientI ->
      val ingredient = ingredientI.copy(name = ingredientI.name.lowercase())

      val id = ingredientDao.insert(ingredient)
      val ingredientId = if (id == -1L) {
        // Load the ingredient if it already exists
        ingredientDao.loadByName(ingredient.name)?.ingredientId ?: 0L
      } else id

      if (ingredientId != 0L) {
        // Assign ingredient to the recipe
        crossRefDao.insert(RecipeIngredientCrossRef(recipeId, ingredientId))
      }
    }

    // Checking whether any new ingredients need to be inserted
    tags.forEach { tagI ->
      val tag = tagI.copy(name = tagI.name.lowercase())

      val id = tagDao.insert(tag)
      val tagId = if (id == -1L) {
        // Load the tag if it already exists
        tagDao.loadByName(tag.name)?.tagId ?: 0L
      } else id

      if (tagId != 0L) {
        // Assign tag to the recipe
        crossRefDao.insert(RecipeTagCrossRef(recipeId, tagId))
      }
    }

    return recipeId
  }

  // Delete a single meal, and its ingredients and tags if needed
  @Transaction
  suspend fun deleteMeal(recipe: Recipe) {
    // Loading all links between Recipe and Ingredient/Tag
    val ingredientIds: List<Long> = crossRefDao.loadAllRIByRecipe(recipe.recipeId)
      .map { it.ingredientId }
    val tagIds: List<Long> = crossRefDao.loadAllRTByRecipe(recipe.recipeId)
      .map { it.tagId }

    // Delete the recipe inside meal
    recipeDao.delete(recipe)

    // Delete ingredients when needed
    ingredientIds.forEach { ingredientId ->
      ingredientDao.loadById(ingredientId)?.let { ingredient ->
        if (crossRefDao.countIngredientCrossRefsById(ingredientId) == 0L) {
          // if ingredient was only used in current meal,
          // delete it
          ingredientDao.delete(ingredient)
        }
      }
    }

    // Delete tags when needed
    tagIds.forEach { tagId ->
      tagDao.loadById(tagId)?.let { tag ->
        if (crossRefDao.countTagCrossRefsById(tagId) == 0L) {
          // if tag was only used in current meal,
          // delete it
          tagDao.delete(tag)
        }
      }
    }
  }

  // Load a single meal by its id
  suspend fun loadMealById(id: Long): Meal? {
    val result = recipeDao.loadMealById(id)
    return result
  }

  // Load all meals
  suspend fun loadAllMeals(): List<Meal> {
    val result = recipeDao.loadAllMeals()
    return result
  }

  // Load all tags
  suspend fun loadAllTags(): List<Tag> {
    val result = tagDao.loadAll()
    return result
  }

  // Load all ingredients
  suspend fun loadAllIngredients(): List<Ingredient> {
    val result = ingredientDao.loadAll().toList()
    return result
  }

  // Delete all records from the database
  @Transaction
  suspend fun clearDatabase() {
    recipeDao.clearTable()
    tagDao.clearTable()
    ingredientDao.clearTable()
    crossRefDao.clearTableRI()
    crossRefDao.clearTableRT()
  }
}