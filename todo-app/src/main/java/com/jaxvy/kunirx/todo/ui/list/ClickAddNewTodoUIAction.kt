package com.jaxvy.kunirx.todo.ui.list

import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.ui.TodoActivityUIState
import dagger.Reusable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class ClickAddNewTodoUIAction @Inject constructor() :
    UIAction<TodoActivityUIState,
            ClickAddNewTodoUIAction.Input,
            ClickAddNewTodoUIAction.ClickAddNewTodoUIActionMutator> {

    class Input : UIAction.Input

    sealed class ClickAddNewTodoUIActionMutator : UIAction.UIStateMutator {
        object NoMutation : ClickAddNewTodoUIActionMutator()
    }

    override fun execute(input: Input): Observable<ClickAddNewTodoUIActionMutator> {
        //TODO: Go to add new todo activity
        return Observable.just(ClickAddNewTodoUIActionMutator.NoMutation)
    }

    override fun reduce(
        uiState: TodoActivityUIState,
        uiStateMutator: ClickAddNewTodoUIActionMutator
    ): TodoActivityUIState {
        return when (uiStateMutator) {
            is ClickAddNewTodoUIActionMutator -> uiState
        }
    }
}
