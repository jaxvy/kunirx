package com.jaxvy.kunirx.todo

import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.repository.TodoRepository
import com.jaxvy.kunirx.todo.ui.list.TodoActivityUIState
import com.jaxvy.kunirx.todo.ui.list.action.OpenTodosViewUIAction
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class OpenTodosViewUIActionTest {

    private val todos = listOf(
        Todo(id = 1, description = "description 1"),
        Todo(id = 2, description = "description 2"),
        Todo(id = 3, description = "description 3")
    )

    private val todoRepository: TodoRepository = mock {
        on { getAll() }.thenReturn(todos)
    }

    private val openTodosViewUIAction = OpenTodosViewUIAction(todoRepository)

    @Test
    fun testExecute() {
        val input = OpenTodosViewUIAction.Input()

        // We need to .await() for the delay in the UIAction to complete
        val observer = openTodosViewUIAction.execute(input).test().await()

        observer.assertValueCount(2)
        observer.assertValueAt(0) { it is OpenTodosViewUIAction.GetTodosMutator.Start }
        observer.assertValueAt(1) {
            it is OpenTodosViewUIAction.GetTodosMutator.Result &&
                    it.todos.size == todos.size &&
                    it.todos[0] == todos[0] &&
                    it.todos[1] == todos[1] &&
                    it.todos[2] == todos[2]
        }
        observer.assertComplete()
        observer.assertNoErrors()

        verify(todoRepository).getAll()
        verify(todoRepository, never()).mark(any(), any())
        verify(todoRepository, never()).create(any())
    }

    @Test
    fun testReduceStart() {
        val initialUIState = TodoActivityUIState(todos = emptyList(), isFetchingTodos = false)
        val startMutator = OpenTodosViewUIAction.GetTodosMutator.Start
        val updatedUIState = openTodosViewUIAction.reduce(initialUIState, startMutator)
        assert(updatedUIState.isFetchingTodos)
    }

    @Test
    fun testReduceResult() {
        val initialUIState = TodoActivityUIState(todos = emptyList(), isFetchingTodos = false)
        val startMutator = OpenTodosViewUIAction.GetTodosMutator.Result(
            todos = todos
        )
        val updatedUIState = openTodosViewUIAction.reduce(initialUIState, startMutator)
        assert(!updatedUIState.isFetchingTodos)
        assert(updatedUIState.todos == todos)
    }
}