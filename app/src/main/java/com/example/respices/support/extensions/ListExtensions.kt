package com.example.respices.support.extensions

fun List<String>.getTopNSimilarity(n: Int, compareTo: String): List<String> {
  val result = mutableListOf<String>()

  // iterating through all items
  this.forEach { item ->
    var curId = result.size - 1

    // caching value to prevent redundant calculations
    val itemSimilarity = item.getSimilarity(compareTo)

    if (result.isEmpty()) {
      // if no elements in the result,
      // add current element right away
      result.add(item)
    }
    else if (result.size < n ||
             result[curId].getSimilarity(compareTo) < itemSimilarity) {
      // if either result has not reached full capacity,
      // OR current element should be in the list
      if (result.size == n) {
        // if result is at full capacity,
        // delete the about-to-be-excess(last) element
        result.removeAt(result.size - 1)
        curId--
      }

      // iterating through result
      while (curId >= 0) {
        if (result[curId].getSimilarity(compareTo) >= itemSimilarity) {
          // insert current element if it should be directly after the result element
          result.add(curId + 1, item)
          break
        }
        else if (curId == 0) {
          // add the element if it should be first
          result.add(0, item)
        }

        curId--
      }
    }
  }

  return result.toList()
}



//  this.forEach { item ->
//    var curId = result.size - 1
//    while (curId >= 0) {
//      if (result[curId].getSimilarity(compareTo) >= item.getSimilarity(compareTo)) {
//        result.add(curId + 1, item)
//        if (result.size > n) {
//          result.removeAt(result.size - 1)
//        }
//        return@forEach
//      }
//
//      curId--
//    }
//
//    result.add(0, item)
//
//    if (result.size > n) {
//      result.removeAt(result.size - 1)
//    }
//  }