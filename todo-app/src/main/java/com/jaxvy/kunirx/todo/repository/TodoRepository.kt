package com.jaxvy.kunirx.todo.repository

import com.jaxvy.kunirx.todo.model.Todo
import dagger.Reusable
import java.util.*
import javax.inject.Inject

@Reusable
class TodoRepository @Inject constructor() {

    private val todos: MutableList<Todo> = mutableListOf()

    init {
        (1..10).forEach { create("todo item$it") }
    }

    fun create(text: String) {
        todos.add(Todo(id = Date().time, text = text))
    }

    fun getById(id: Long): Todo? = todos.find { it.id == id }

    fun getAll(): List<Todo> = todos

    fun markAsComplete(id: Long) {
        getById(id)?.run { isComplete = true }
    }

    fun markAsUnComplete(id: Long) {
        getById(id)?.run { isComplete = false }
    }

    fun updateText(id: Long, newText: String) {
        getById(id)?.run { text = newText }
    }
}