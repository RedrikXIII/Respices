package com.example.respices.support.extensions

import com.example.respices.storage.entities.RecipeWithIngredientsAndTags

// Priority:
/*
One-offs: pass or not, if first has and second doesn't first ALWAYS wins
is within time
are all ingredients there (<=)
are all tags there (<=)

Compare amount:
amount of ingredients matching(0-1, 1 - all ingredients in base are present in compared)
amount of tags matching(0-1, 1 - all tags in base are present in compared)
biggest preference wins

 */

fun RecipeWithIngredientsAndTags.toString2(): String {
  var result: String = "{${this.recipe.name}, ${this.recipe.time}, ${this.recipe.rating}, " +
          "${this.recipe.link}, ${this.recipe.steps}, "

  result += "ingredients: ("
  this.ingredients.forEach{ ing ->
    result += "${ing.name} "
  }

  result += " ), tags: ("
  this.tags.forEach{ tag ->
    result += "${tag.name} "
  }
  result += " )}"

  return result
}
