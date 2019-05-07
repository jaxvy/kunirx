package com.jaxvy.kunirx.todo.ui.list.action

import android.app.Activity
import android.content.Intent
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.ui.create.TodoCreateActivity
import com.jaxvy.kunirx.todo.ui.list.TodoActivityUIState
import com.jaxvy.kunirx.todo.utils.KunirxSideEffectUIAction
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ClickCreateTodoButtonUIAction @Inject constructor() :
    KunirxSideEffectUIAction<TodoActivityUIState, ClickCreateTodoButtonUIAction.Input>() {

    class Input(val activity: Activity) : UIAction.Input

    override fun sideEffect(input: Input) {
        input.activity.startActivity(Intent(input.activity, TodoCreateActivity::class.java))
    }
}
