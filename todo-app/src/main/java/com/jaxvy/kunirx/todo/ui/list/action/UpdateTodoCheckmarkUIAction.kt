package com.jaxvy.kunirx.todo.ui.list.action

import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.repository.TodoRepository
import com.jaxvy.kunirx.todo.ui.list.TodoActivityUIState
import com.jaxvy.kunirx.todo.ui.list.action.UpdateTodoCheckmarkUIAction.Input
import com.jaxvy.kunirx.todo.ui.list.action.UpdateTodoCheckmarkUIAction.UIStateMutator
import dagger.Reusable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class UpdateTodoCheckmarkUIAction @Inject constructor(
    private val todoRepository: TodoRepository
) : UIAction<TodoActivityUIState, Input, UIStateMutator> {

    class Input(val id: Long, val isChecked: Boolean) : UIAction.Input

    sealed class UIStateMutator : UIAction.UIStateMutator {
        class Result(val todos: List<Todo>) : UIStateMutator()
    }

    override fun execute(
        input: Input
    ): Observable<UIStateMutator> {
        todoRepository.mark(input.id, input.isChecked)
        return Observable.just<UIStateMutator>(
            UIStateMutator.Result(
                todoRepository.getAll()
            )
        )
    }

    override fun reduce(
        uiState: TodoActivityUIState,
        uiStateMutator: UIStateMutator
    ): TodoActivityUIState {
        return when (uiStateMutator) {
            is UIStateMutator.Result -> uiState.copy(todos = uiStateMutator.todos)
        }
    }
}
