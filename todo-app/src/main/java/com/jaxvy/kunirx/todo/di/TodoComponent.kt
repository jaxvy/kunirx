package com.jaxvy.kunirx.todo.di

import com.jaxvy.kunirx.todo.ui.TodoActivity
import com.jaxvy.kunirx.todo.TodoApplication
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TodoModule::class])
interface TodoComponent {

    fun inject(TodoActivity: TodoActivity)
}

fun TodoActivity.inject(TodoActivity: TodoActivity) {
    (applicationContext as TodoApplication).todoComponent.inject(TodoActivity)
}
