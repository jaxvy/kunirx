package com.jaxvy.kunirx.todo.repository

import com.jaxvy.kunirx.todo.model.Todo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor() {

    private val todos: MutableList<Todo> = mutableListOf()

    init {
        (1..5).forEach { create(description = "Todo description $it") }
    }

    fun create(description: String) {
        todos.add(Todo(id = todos.size.toLong(), description = description))
    }

    fun getAll(): List<Todo> = todos

    fun mark(id: Long, complete: Boolean) {
        todos.find { it.id == id }
            ?.run { isComplete = complete }
    }
}