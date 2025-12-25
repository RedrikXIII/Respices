package com.example.respices.support.classes

import kotlinx.serialization.Serializable

@Serializable
data class MealData(
  val name: String = "",
  val time: Long = 0L,
  val rating: Double = 0.0,
  val steps: String = "",
  val link: String = "",
  val ingredients: List<String> = listOf(),
  val tags: List<String> = listOf()
)
