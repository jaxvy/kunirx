package com.jaxvy.kunirx.todo.ui.create

import android.os.Bundle
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.UIActionHandler
import com.jaxvy.kunirx.UIState
import com.jaxvy.kunirx.UIViewActivity
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.TodoApplication
import com.jaxvy.kunirx.todo.di.IOScheduler
import com.jaxvy.kunirx.todo.di.MainScheduler
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

data class TodoCreateActionUIState(
    val description: String
) : UIState

class TodoCreateActivity() : UIViewActivity<TodoCreateActionUIState>() {

    @Inject
    lateinit var uiActionHandlerConfig: UIActionHandlerConfig

    override fun uiActionHandlerConfiguration() = uiActionHandlerConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as TodoApplication).todoComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_create)

        uiState = TodoCreateActionUIState(
            description = ""
        )
    }

    override fun uiActionInputObservable(): Observable<UIAction.Input> {
        return Observable.empty()
    }

    override fun render(uiState: TodoCreateActionUIState) {

    }

}

@Reusable
class UIActionHandlerConfig @Inject constructor(
    @MainScheduler override var mainScheduler: Scheduler,
    @IOScheduler override var ioScheduler: Scheduler
) : UIActionHandler.Configuration(
    uiActions = listOf(
    )
)
