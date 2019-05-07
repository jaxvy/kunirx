package com.jaxvy.kunirx.todo.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.UIActionHandler
import com.jaxvy.kunirx.UIState
import com.jaxvy.kunirx.UIViewActivity
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.di.IOScheduler
import com.jaxvy.kunirx.todo.di.MainScheduler
import com.jaxvy.kunirx.todo.di.inject
import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.ui.list.ClickAddNewTodoUIAction
import com.jaxvy.kunirx.todo.ui.list.OpenTodosViewUIAction
import com.jaxvy.kunirx.todo.ui.list.UpdateTodoCheckmarkUIAction
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_todo.*
import javax.inject.Inject

data class TodoActivityUIState(
    val todos: List<Todo>,
    val isFetchingTodos: Boolean = false
) : UIState

class TodoActivity : UIViewActivity<TodoActivityUIState>() {

    @Inject
    lateinit var uiUIActionHandlerConfig: UIActionHandlerConfig

    override fun uiActionHandlerConfiguration() = uiUIActionHandlerConfig

    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        uiState = TodoActivityUIState(
            todos = emptyList()
        )

        todoListAdapter = TodoListAdapter()
        todoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TodoActivity)
            adapter = todoListAdapter
        }
    }

    override fun render(uiState: TodoActivityUIState) {
        if (uiState.isFetchingTodos) {
            loadingProgressBar.visibility = View.VISIBLE
        } else {
            loadingProgressBar.visibility = View.GONE

            todoListAdapter.update(todos = uiState.todos)
        }
    }

    override fun uiActionInputObservable(): Observable<UIAction.Input> {
        return Observable.mergeArray(
            todoListAdapter.uiActionInputObservable(),

            addNewTodoButton.clicks()
                .map { ClickAddNewTodoUIAction.Input() }
        ).startWith(OpenTodosViewUIAction.Input())
    }
}

@Reusable
class UIActionHandlerConfig @Inject constructor(
    @MainScheduler override var mainScheduler: Scheduler,
    @IOScheduler override var ioScheduler: Scheduler,
    clickAddNewTodoUIAction: ClickAddNewTodoUIAction,
    openTodosViewUIAction: OpenTodosViewUIAction,
    updateTodoCheckmarkUIAction: UpdateTodoCheckmarkUIAction
) : UIActionHandler.Configuration(
    uiActions = listOf(
        clickAddNewTodoUIAction,
        openTodosViewUIAction,
        updateTodoCheckmarkUIAction
    )
)
