package com.example.respices.support.utility

// Stack Dynamic Data Structure
class Stack<E> {
  // MutableList under the hood
  private val _stack: MutableList<E> = mutableListOf()

  // Push an element to the stack
  fun push(elem: E) {
    _stack.add(elem)
  }

  // Pop an element from the stack
  fun pop(): E? {
    if (_stack.isEmpty())
      return null
    else {
      val res = _stack[_stack.size - 1]
      _stack.removeAt(_stack.size - 1)
      return res
    }
  }

  // Determine whether stack is empty
  fun isEmpty(): Boolean {
    return _stack.isEmpty()
  }

  // Clear the stack
  fun clear() {
    _stack.clear()
  }

  // Convert the stack to string
  override fun toString(): String {
    return _stack.toString()
  }
}