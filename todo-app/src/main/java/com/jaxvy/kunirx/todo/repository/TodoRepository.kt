package com.jaxvy.kunirx.todo.repository

import com.jaxvy.kunirx.todo.model.Todo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor() {

    private val todos: MutableList<Todo> = mutableListOf()

    init {
        (1..5).forEach { create(id = it.toLong(), text = "todo item$it") }
    }

    fun create(id: Long, text: String) {
        todos.add(Todo(id = id, text = text))
    }

    fun getById(id: Long): Todo? = todos.find { it.id == id }

    fun getAll(): List<Todo> = todos

    fun markAsComplete(id: Long) {
        getById(id)?.run { isComplete = true }
    }

    fun markAsIncomplete(id: Long) {
        getById(id)?.run { isComplete = false }
    }

    fun updateText(id: Long, newText: String) {
        getById(id)?.run { text = newText }
    }
}