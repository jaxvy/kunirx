package com.jaxvy.kunirx.todo.ui.create

import android.os.Bundle
import com.jakewharton.rxbinding3.view.clicks
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.UIActionHandler
import com.jaxvy.kunirx.UIState
import com.jaxvy.kunirx.UIViewActivity
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.TodoApplication
import com.jaxvy.kunirx.todo.di.IOScheduler
import com.jaxvy.kunirx.todo.di.MainScheduler
import com.jaxvy.kunirx.todo.ui.create.action.SaveDescriptionUIAction
import com.jaxvy.kunirx.todo.ui.create.action.UpdateDescriptionUIAction
import com.jaxvy.kunirx.todo.utils.setTextSilently
import com.jaxvy.kunirx.todo.utils.textChangesWhenFocused
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_todo_create.*
import java.lang.ref.WeakReference
import javax.inject.Inject

data class TodoCreateActivityUIState(
    val description: String
) : UIState

class TodoCreateActivity() : UIViewActivity<TodoCreateActivityUIState>() {

    @Inject
    lateinit var uiActionHandlerConfig: UIActionHandlerConfig

    override fun uiActionHandlerConfiguration() = uiActionHandlerConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as TodoApplication).todoComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_create)

        uiState = TodoCreateActivityUIState(
            description = ""
        )
    }

    override fun uiActionInputObservable(): Observable<UIAction.Input> {
        return Observable.mergeArray(
            saveButton.clicks()
                .map {
                    SaveDescriptionUIAction.Input(
                        description = uiState.description,
                        activityWeakReference = WeakReference(this)
                    )
                },

            descriptionEditText.textChangesWhenFocused()
                .skipInitialValue()
                .map { UpdateDescriptionUIAction.Input(description = it.text) }
        )
    }

    override fun render(uiState: TodoCreateActivityUIState) {
        descriptionEditText.setTextSilently(uiState.description)
    }
}

@Reusable
class UIActionHandlerConfig @Inject constructor(
    @MainScheduler override var mainScheduler: Scheduler,
    @IOScheduler override var ioScheduler: Scheduler,
    private val updateDescriptionUIAction: UpdateDescriptionUIAction,
    private val saveDescriptionUIAction: SaveDescriptionUIAction
) : UIActionHandler.Configuration(
    uiActions = listOf(
        updateDescriptionUIAction,
        saveDescriptionUIAction
    )
)
