package com.example.respices.support.extensions

import android.util.Log

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

fun String.replaceTyping(newStr: String): String {
  if (newStr.length - this.length == 1) {
    this.forEachIndexed { index, value ->
      if (value != newStr[index]) {
        var res: String = newStr.substring(startIndex = 0, endIndex = index + 1)
        if (index < this.length - 1) {
          res += newStr.substring(startIndex = index + 2, endIndex = newStr.length)
        }

        return res
      }
    }
  }

  return newStr
}

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