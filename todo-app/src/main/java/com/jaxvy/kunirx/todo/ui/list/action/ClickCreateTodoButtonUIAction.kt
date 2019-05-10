package com.jaxvy.kunirx.todo.ui.list.action

import android.app.Activity
import android.content.Intent
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.ui.create.TodoCreateActivity
import com.jaxvy.kunirx.todo.ui.list.TodoActivityUIState
import com.jaxvy.kunirx.todo.ui.list.action.ClickCreateTodoButtonUIAction.Input
import com.jaxvy.kunirx.utils.KunirxSideEffectUIAction
import dagger.Reusable
import java.lang.ref.WeakReference
import javax.inject.Inject

@Reusable
class ClickCreateTodoButtonUIAction @Inject constructor() :
    KunirxSideEffectUIAction<TodoActivityUIState, Input>() {

    class Input(val activityWeakReference: WeakReference<Activity>) : UIAction.Input

    override fun sideEffect(input: Input) {
        input.activityWeakReference.get()
            ?.run { startActivity(Intent(this, TodoCreateActivity::class.java)) }
    }
}
