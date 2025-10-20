package com.example.respices.support.extensions

fun List<String>.getTopNSimilarity(n: Int, compareTo: String): List<String> {
  val result = mutableListOf<String>()

  this.forEach { item ->
    var curId = result.size - 1
    while (curId >= 0) {
      if (result[curId].getSimilarity(compareTo) >= item.getSimilarity(compareTo)) {
        result.add(curId + 1, item)
        if (result.size > n) {
          result.removeAt(result.size - 1)
        }
        return@forEach
      }

      curId--
    }

    result.add(0, item)

    if (result.size > n) {
      result.removeAt(result.size - 1)
    }

  }

  return result.toList()
}