package com.example.respices.support.utility

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class EventHandler<T> {
  private var events: MutableMap<Uuid, (T) -> Unit> = mutableMapOf()

  fun subscribe(func: (T) -> Unit): Uuid {
    val newId = Uuid.random()
    events[newId] = func
    return newId
  }

  fun unsubscribe(id: Uuid): Boolean {
    if (events.containsKey(id)) {
      events.remove(id)
      return true
    }

    return false
  }

  fun invoke(invokeValue: T) {
    events.forEach{(key, value) ->
      value.invoke(invokeValue)
    }
  }
}