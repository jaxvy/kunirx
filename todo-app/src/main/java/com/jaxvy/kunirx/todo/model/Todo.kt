package com.jaxvy.kunirx.todo.model

data class Todo(
    val id: Long,
    var description: String,
    var isComplete: Boolean = false
)