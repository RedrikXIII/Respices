package com.example.respices.support.extensions

fun String.getSimilarity(str2: String): Double {
  var result: Double = 0.0
  result -= this.length
  result -= str2.length

  val map: MutableMap<Char, Int> = mutableMapOf()
  this.forEach { c ->
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