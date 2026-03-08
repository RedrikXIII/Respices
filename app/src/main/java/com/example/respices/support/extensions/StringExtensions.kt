package com.example.respices.support.extensions

import com.example.respices.support.classes.MealData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

// Calculate similarity between 2 strings
fun String.getSimilarity(str2I: String): Double {
  val str1 = this.lowercase()
  val str2 = str2I.lowercase()

  var result: Double = 0.0
  result -= str1.length
  result -= str2.length

  val map: MutableMap<Char, Int> = mutableMapOf()
  str1.forEach { c ->
    map[c] = (map[c] ?: 0) + 1
  }

  str2.forEach { c ->
    if ((map[c] ?: 0) > 0) {
      result += 6
      map[c] = map[c]!! - 1
    }
  }

  return result
}

// Return a string from typing as if it was in "insert" mode
fun String.replaceTyping(newStr: String, cursor: Int): String {
  if (cursor >= 0 && cursor <= newStr.length) {
    if (newStr.length - this.length == 1) {
      var res: String = newStr.substring(startIndex = 0, endIndex = cursor)
      if (cursor < newStr.length - 1) {
        res += newStr.substring(startIndex = cursor + 1, endIndex = newStr.length)
      }

      return res
    }
  }

  return newStr
}

// Validate a string representation of list of MealData
fun String.isValidMealDataList(): Boolean {
  return try {
    Json.decodeFromString(
      ListSerializer(MealData.serializer()),
      this
    )
    true
  }
  catch (e: SerializationException) {
    false
  }
}