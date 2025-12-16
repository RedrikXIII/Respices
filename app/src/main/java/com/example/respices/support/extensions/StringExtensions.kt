package com.example.respices.support.extensions

import android.util.Log

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