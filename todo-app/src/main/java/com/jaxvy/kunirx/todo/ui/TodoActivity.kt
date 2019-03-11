package com.jaxvy.kunirx.todo.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.UIActionHandler
import com.jaxvy.kunirx.UIState
import com.jaxvy.kunirx.UIViewActivity
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.di.IOScheduler
import com.jaxvy.kunirx.todo.di.MainScheduler
import com.jaxvy.kunirx.todo.di.inject
import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.ui.action.OpenTodosViewUIAction
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_todo.*
import javax.inject.Inject

data class TodoActivityUIState(
    val todos: List<Todo>,
    val isFetchingTodos: Boolean = false
) : UIState

@Reusable
class TodoActivityUIActionConfig @Inject constructor(
    @MainScheduler override var mainScheduler: Scheduler,
    @IOScheduler override var ioScheduler: Scheduler,
    openTodosViewUIAction: OpenTodosViewUIAction
) : UIActionHandler.Configuration(
    uiActions = listOf(openTodosViewUIAction)
)

class TodoActivity : UIViewActivity<TodoActivityUIState>() {

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        uiState = TodoActivityUIState(
            todos = emptyList()
        )

        todoAdapter = TodoAdapter()
        todoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TodoActivity)
            adapter = todoAdapter
        }
    }

    @Inject
    lateinit var uiActionConfig: TodoActivityUIActionConfig

    override fun uiActionHandlerConfiguration(): UIActionHandler.Configuration = uiActionConfig

    override fun render(uiState: TodoActivityUIState) {
        if (uiState.isFetchingTodos) {
            loadingProgressBar.visibility = View.VISIBLE
        } else {
            loadingProgressBar.visibility = View.GONE

            todoAdapter.update(todos = uiState.todos)
        }
    }

    override fun uiActionInputObservable(): Observable<UIAction.Input> {
        return Observable.just(OpenTodosViewUIAction.Input())
    }
}
