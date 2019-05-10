package com.jaxvy.kunirx.todo.ui.create.action

import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.ui.create.TodoCreateActivityUIState
import com.jaxvy.kunirx.todo.ui.create.action.UpdateDescriptionUIAction.Input
import com.jaxvy.kunirx.todo.ui.create.action.UpdateDescriptionUIAction.UIStateMutator
import dagger.Reusable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class UpdateDescriptionUIAction @Inject constructor() :
    UIAction<TodoCreateActivityUIState, Input, UIStateMutator> {

    class Input(val description: String) : UIAction.Input

    override fun execute(input: Input): Observable<UIStateMutator> {
        return Observable.just(UIStateMutator.Result(input.description))
    }

    sealed class UIStateMutator : UIAction.UIStateMutator {
        class Result(val description: String) : UIStateMutator()
    }

    override fun reduce(
        uiState: TodoCreateActivityUIState,
        uiStateMutator: UIStateMutator
    ): TodoCreateActivityUIState {
        return when (uiStateMutator) {
            is UIStateMutator.Result -> uiState.copy(description = uiStateMutator.description)
        }
    }
}