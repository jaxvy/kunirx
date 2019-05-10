package com.jaxvy.kunirx.todo.ui.create.action

import android.app.Activity
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.repository.TodoRepository
import com.jaxvy.kunirx.todo.ui.create.TodoCreateActivityUIState
import com.jaxvy.kunirx.todo.ui.create.action.SaveDescriptionUIAction.Input
import com.jaxvy.kunirx.utils.KunirxSideEffectUIAction
import dagger.Reusable
import java.lang.ref.WeakReference
import javax.inject.Inject

@Reusable
class SaveDescriptionUIAction @Inject constructor(
    private val todosRepository: TodoRepository
) : KunirxSideEffectUIAction<TodoCreateActivityUIState, Input>() {

    class Input(
        val description: String,
        val activityWeakReference: WeakReference<Activity>
    ) : UIAction.Input

    override fun sideEffect(input: Input) {
        with(input) {
            todosRepository.create(description)
            activityWeakReference.get()?.finish()
        }
    }
}