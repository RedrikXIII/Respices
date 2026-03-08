package com.example.respices.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.storage.repositories.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// RecipeView Model - UI connection to the Database & Cache of loaded data
class RecipeViewModel(
  private val repository: RecipeRepository,
  application: Application
) : AndroidViewModel(application) {
  // Context
  private val appContext: Context = getApplication<Application>().applicationContext

  // Meal Cache
  private val _allMeals = MutableStateFlow<List<Meal>>(emptyList())
  val allMeals: StateFlow<List<Meal>> = _allMeals

  // Ingredient Cache
  private val _allIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
  val allIngredients: StateFlow<List<Ingredient>> = _allIngredients

  // Tag Cache
  private val _allTags = MutableStateFlow<List<Tag>>(emptyList())
  val allTags: StateFlow<List<Tag>> = _allTags

  // Load all meals from the database
  fun loadAllMeals(onComplete: (List<Meal>) -> Unit = {}) {
    // launching asynchronous execution
    viewModelScope.launch {
      // loading all meals from the database
      val result = repository.loadAllMeals()

      // updating ingredients and tags
      loadAllIngredients()
      loadAllTags()

      // executing callback with the new meals added
      _allMeals.value = result
      onComplete.invoke(allMeals.value)
    }
  }

  // Load & Cache all ingredients
  fun loadAllIngredients() {
    viewModelScope.launch {
      val result = repository.loadAllIngredients()
      _allIngredients.value = result
    }
  }

  // Load & Cache all tags
  fun loadAllTags() {
    viewModelScope.launch {
      val result = repository.loadAllTags()
      _allTags.value = result
    }
  }

  // Insert a single meal
  fun insertMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>, onComplete: (Long) -> Unit = {}) {
    viewModelScope.launch {
      val res = repository.insertMeal(recipe, ingredients, tags)
      loadAllMeals {
        onComplete.invoke(res)
      }
    }
  }

  // Delete a single meal
  fun deleteMeal(recipe: Recipe, onComplete: () -> Unit = {}) {
    viewModelScope.launch {
      repository.deleteMeal(recipe)
      loadAllMeals {
        onComplete.invoke()
      }
    }
  }

  // Update/Insert a single meal
  fun upsertMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>, onComplete: (Long) -> Unit = {}) {
    deleteMeal(recipe) {
      insertMeal(recipe, ingredients, tags) { id ->
        onComplete.invoke(id)
      }
    }
  }

  // Clear database
  fun clearAll(onComplete: () -> Unit) {
    viewModelScope.launch {
      repository.clearDatabase()
      onComplete.invoke()
    }
  }
}
