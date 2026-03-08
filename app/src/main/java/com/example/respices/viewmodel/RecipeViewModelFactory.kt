package com.example.respices.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.respices.storage.repositories.RecipeRepository

// Factory of RecipeViewModel
class RecipeViewModelFactory(
  private val repository: RecipeRepository,
  private val application: Application
) : ViewModelProvider.Factory {

  // Create an instance of RecipeViewModel
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return RecipeViewModel(repository, application) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}