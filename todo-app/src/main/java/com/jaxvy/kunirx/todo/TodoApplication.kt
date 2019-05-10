package com.jaxvy.kunirx.todo

import android.app.Application
import com.jaxvy.kunirx.todo.di.DaggerTodoComponent

import com.jaxvy.kunirx.todo.di.TodoComponent
import com.jaxvy.kunirx.todo.di.TodoModule

class TodoApplication : Application() {

    lateinit var todoComponent: TodoComponent

    override fun onCreate() {
        super.onCreate()

        todoComponent = DaggerTodoComponent.builder()
            .todoModule(TodoModule(applicationContext))
            .build()

    }
}
