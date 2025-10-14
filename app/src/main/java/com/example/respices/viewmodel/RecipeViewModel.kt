package com.example.respices.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.RecipeWithIngredientsAndTags
import com.example.respices.storage.entities.Tag
import com.example.respices.storage.repositories.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel(
  private val repository: RecipeRepository
) : ViewModel() {

  // UI State
  private val _allRecipes = MutableStateFlow<List<RecipeWithIngredientsAndTags>>(emptyList())
  val allRecipes: StateFlow<List<RecipeWithIngredientsAndTags>> = _allRecipes

  private val _allIngredients = MutableStateFlow<List<Ingredient>>(emptyList())
  val allIngredients: StateFlow<List<Ingredient>> = _allIngredients

  private val _allTags = MutableStateFlow<List<Tag>>(emptyList())
  val allTags: StateFlow<List<Tag>> = _allTags

  private val _currentRecipe = MutableStateFlow<RecipeWithIngredientsAndTags?>(null)
  val currentRecipe: StateFlow<RecipeWithIngredientsAndTags?> = _currentRecipe

  // Load Data
  fun loadAllRecipes() {
    viewModelScope.launch {
      _allRecipes.value = repository.loadAllRWIATs()
    }
  }

  fun loadAllIngredients() {
    viewModelScope.launch {
      _allIngredients.value = repository.loadAllIngredients()
    }
  }

  fun loadAllTags() {
    viewModelScope.launch {
      _allTags.value = repository.loadAllTags()
    }
  }

  fun loadRecipeById(id: Long) {
    viewModelScope.launch {
      _currentRecipe.value = repository.loadRWIATById(id)
    }
  }

  // Insert/Delete Recipe
  fun insertRecipe(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>) {
    viewModelScope.launch {
      repository.insertRWIAT(recipe, ingredients, tags)
      loadAllRecipes() // refresh UI state
    }
  }

  fun deleteRecipe(recipe: Recipe, ingredients: List<Ingredient>, tags: List<Tag>) {
    viewModelScope.launch {
      repository.deleteRWIAT(recipe, ingredients, tags)
      loadAllRecipes() // refresh UI state
    }
  }
}
