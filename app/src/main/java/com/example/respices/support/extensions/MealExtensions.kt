package com.example.respices.support.extensions

import com.example.respices.storage.entities.Ingredient
import com.example.respices.storage.entities.Meal
import com.example.respices.storage.entities.Recipe
import com.example.respices.storage.entities.Tag
import com.example.respices.support.enums.MealFault

// Priority:
/*
One-offs: pass or not, if first has and second doesn't first ALWAYS wins
is within time
are all ingredients there (<=)

Compare amount:
amount of ingredients matching(0-1, 1 - all ingredients in base are present in compared)
amount of tags matching(0-1, 1 - all tags in base are present in compared)
biggest preference wins

 */

fun Meal.toString2(): String {
  var result: String = "{${this.recipe.name}, ${this.recipe.time}, ${this.recipe.rating}, " +
          "${this.recipe.link}, ${this.recipe.steps}, "

  result += "ingredients: ("
  this.ingredients.forEach{ ing ->
    result += ing.name
    if (ing.ingredientId != this.ingredients.last().ingredientId) {
      result += " "
    }
  }

  result += "), tags: ("
  this.tags.forEach{ tag ->
    result += tag.name
    if (tag.tagId != this.tags.last().tagId) {
      result += " "
    }
  }
  result += ")}"

  return result
}

fun Meal.isSelected(time: Long, ingredients: List<Ingredient>): Boolean {
  if (this.recipe.time > time)
    return false

  this.ingredients.forEach { ing ->
    if (ingredients.find { ing2 -> ing.name.equals(ing2.name, true)} == null) {
      return false
    }
  }

  return true
}

fun Meal.acceptanceIndex(tags: List<Tag>): Double {
  var result: Double = 1.0

  if (tags.size > 0) {
    var tags_matches: Double = 0.0
    tags.forEach { tag ->
      if (this.tags.find { tag2 -> tag.name.equals(tag2.name, true)} != null) {
        tags_matches++
      }
    }

    result *= (tags_matches * 1.0 / tags.size) + 1
  }

  result *= (this.recipe.rating / 10.0) + 1

  return result
}

fun Meal.isValid(): Pair<Boolean, MealFault> {
  // Name -> Ingredients -> Time
  if (this.recipe.name.trim() == "") {
    return Pair(false, MealFault.NO_NAME)
  }

  if (this.ingredients.isEmpty()) {
    return Pair(false, MealFault.NO_INGREDIENTS)
  }

  if (this.recipe.time == 0L) {
    return Pair(false, MealFault.NO_TIME)
  }

  return Pair(true, MealFault.NONE)
}

fun Meal.isEmpty(): Boolean {
  return this.recipe.name == "" &&
          this.recipe.time == 0L &&
          this.recipe.rating == 0.0 &&
          this.recipe.link == "" &&
          this.recipe.steps == "" &&
          this.ingredients.isEmpty() &&
          this.tags.isEmpty()
}

fun getEmptyMeal(): Meal {
  return Meal(
    recipe = Recipe(
      name = "",
      time = 0,
      rating = 0.0,
      link = "",
      steps = ""
    ),
    ingredients = listOf(),
    tags = listOf()
  )
}