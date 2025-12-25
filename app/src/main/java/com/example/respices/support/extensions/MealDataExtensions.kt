package com.example.respices.support.extensions

import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.support.classes.MealData

fun MealData.toMeal(): Meal {
  return Meal(
    recipe = Recipe(
      name = this.name,
      time =  this.time,
      rating = this.rating,
      link = this.link,
      steps = this.steps
    ),
    ingredients = this.ingredients.map { name -> Ingredient(name = name) },
    tags = this.tags.map { name -> Tag(name = name) }
  )
}