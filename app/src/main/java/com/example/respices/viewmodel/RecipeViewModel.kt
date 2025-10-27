package com.example.respices.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.storage.repositories.RecipeRepository
import com.example.respices.support.services.LoggerService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(
  private val repository: RecipeRepository,
  application: Application
) : AndroidViewModel(application) {
  // Context
  private val appContext: Context = getApplication<Application>().applicationContext

  // UI State
  private val _allMeals = MutableStateFlow<List<Meal>>(emptyList())
  val allMeals: StateFlow<List<Meal>> = _allMeals

  private val _allIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
  val allIngredients: StateFlow<List<Ingredient>> = _allIngredients

  private val _allTags = MutableStateFlow<List<Tag>>(emptyList())
  val allTags: StateFlow<List<Tag>> = _allTags

  private val _currentRecipe = MutableStateFlow<Meal?>(null)
  val currentRecipe: StateFlow<Meal?> = _currentRecipe

  // Load Data
  fun loadAllMeals() {
    viewModelScope.launch {
      LoggerService.log("ViewModel: loading all meals...", appContext)
      val result = repository.loadAllMeals()
      LoggerService.log("ViewModel: loaded all meals", appContext)
      _allMeals.value = result
    }

    loadAllIngredients()
    loadAllTags()
  }

  fun loadAllIngredients() {
    viewModelScope.launch {
      LoggerService.log("ViewModel: loading all ingredients...", appContext)
      val result = repository.loadAllIngredients()
      LoggerService.log("ViewModel: loaded all ingredients", appContext)
      _allIngredients.value = result
    }
  }

  fun loadAllTags() {
    viewModelScope.launch {
      LoggerService.log("ViewModel: loading all tags...", appContext)
      val result = repository.loadAllTags()
      LoggerService.log("ViewModel: loaded all ingredients", appContext)
      _allTags.value = result
    }
  }

  fun loadMealById(id: Long) {
    viewModelScope.launch {
      LoggerService.log("ViewModel: loading a meal by id...", appContext)
      val result = repository.loadMealById(id)
      LoggerService.log("ViewModel: loaded a meal by id", appContext)
      _currentRecipe.value = result
    }
  }

  // Insert/Delete Recipe
  fun insertMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>) {
    viewModelScope.launch {
      LoggerService.log("ViewModel: inserting a meal...", appContext)
      repository.insertMeal(recipe, ingredients, tags)
      LoggerService.log("ViewModel: inserted a meal", appContext)
      loadAllMeals() // refresh UI state
    }
  }

  fun deleteMeal(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>) {
    viewModelScope.launch {
      LoggerService.log("ViewModel: deleting a meal...", appContext)
      repository.deleteMeal(recipe, ingredients, tags)
      LoggerService.log("ViewModel: deleted a meal", appContext)
      loadAllMeals() // refresh UI state
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
