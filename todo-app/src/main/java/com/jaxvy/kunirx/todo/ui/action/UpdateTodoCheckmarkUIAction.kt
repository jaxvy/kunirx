package com.jaxvy.kunirx.todo.ui.action

import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.repository.TodoRepository
import com.jaxvy.kunirx.todo.ui.TodoActivityUIState
import dagger.Reusable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class UpdateTodoCheckmarkUIAction @Inject constructor(
    private val todoRepository: TodoRepository
) : UIAction<TodoActivityUIState,
        UpdateTodoCheckmarkUIAction.Input,
        UpdateTodoCheckmarkUIAction.UpdateTodoCheckmarkMutator> {

    class Input(val id: Long, val isChecked: Boolean) : UIAction.Input

    sealed class UpdateTodoCheckmarkMutator : UIAction.UIStateMutator {
        class Result(val todos: List<Todo>) : UpdateTodoCheckmarkMutator()
    }

    override fun execute(
        input: UpdateTodoCheckmarkUIAction.Input
    ): Observable<UpdateTodoCheckmarkMutator> {
        if (input.isChecked) {
            todoRepository.markAsComplete(input.id)
        } else {
            todoRepository.markAsUnComplete(input.id)
        }
        return Observable.just<UpdateTodoCheckmarkMutator>(
            UpdateTodoCheckmarkMutator.Result(todoRepository.getAll())
        )
    }

    override fun reduce(
        uiState: TodoActivityUIState,
        uiStateMutator: UpdateTodoCheckmarkMutator
    ): TodoActivityUIState {
        return when (uiStateMutator) {
            is UpdateTodoCheckmarkMutator.Result -> uiState.copy(todos = uiStateMutator.todos)
        }
    }
}
