package com.example.respices.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.respices.storage.repositories.RecipeRepository

class RecipeViewModelFactory(
  private val repository: RecipeRepository
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return RecipeViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}