package com.jaxvy.kunirx.todo.di

import com.jaxvy.kunirx.todo.ui.create.TodoCreateActivity
import com.jaxvy.kunirx.todo.ui.list.TodoListActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TodoModule::class])
interface TodoComponent {
    fun inject(todoListActivity: TodoListActivity)
    fun inject(todoCreateActivity: TodoCreateActivity)
}

