package com.jaxvy.kunirx.todo.ui.list.action

import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.repository.TodoRepository
import com.jaxvy.kunirx.todo.ui.list.TodoActivityUIState
import com.jaxvy.kunirx.todo.ui.list.action.OpenTodosViewUIAction.GetTodosMutator
import com.jaxvy.kunirx.todo.ui.list.action.OpenTodosViewUIAction.Input
import dagger.Reusable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// Delay data load for fun
private const val LOAD_DELAY = 500L

@Reusable
class OpenTodosViewUIAction @Inject constructor(
    private val todoRepository: TodoRepository
) : UIAction<TodoActivityUIState, Input, GetTodosMutator> {

    class Input : UIAction.Input

    sealed class GetTodosMutator : UIAction.UIStateMutator {
        object Start : GetTodosMutator()
        class Result(val todos: List<Todo>) : GetTodosMutator()
    }

    override fun execute(input: Input): Observable<GetTodosMutator> {
        return Observable
            .just<GetTodosMutator>(
                GetTodosMutator.Result(
                    todoRepository.getAll()
                )
            )
            .delay(LOAD_DELAY, TimeUnit.MILLISECONDS)
            .startWith(GetTodosMutator.Start)
    }

    override fun reduce(
        uiState: TodoActivityUIState,
        uiStateMutator: GetTodosMutator
    ): TodoActivityUIState {
        return when (uiStateMutator) {
            is GetTodosMutator.Start -> uiState.copy(isFetchingTodos = true)
            is GetTodosMutator.Result -> uiState.copy(
                isFetchingTodos = false,
                todos = uiStateMutator.todos
            )
        }
    }
}
