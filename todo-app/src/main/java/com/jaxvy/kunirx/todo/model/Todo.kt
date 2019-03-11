package com.jaxvy.kunirx.todo.model

data class Todo(
    val id: Long,
    var text: String,
    var isComplete: Boolean = false
)