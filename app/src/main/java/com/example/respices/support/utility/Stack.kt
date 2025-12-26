package com.example.respices.support.utility

class Stack<E> {

  private val _stack: MutableList<E> = mutableListOf()

  fun push(elem: E) {
    _stack.add(elem)
  }

  fun pop(): E? {
    if (_stack.isEmpty())
      return null
    else {
      val res = _stack[_stack.size - 1]
      _stack.removeAt(_stack.size - 1)
      return res
    }
  }

  fun isEmpty(): Boolean {
    return _stack.isEmpty()
  }

  fun clear() {
    _stack.clear()
  }

  override fun toString(): String {
    return _stack.toString()
  }
}