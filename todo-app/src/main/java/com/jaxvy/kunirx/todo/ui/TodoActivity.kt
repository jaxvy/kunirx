package com.jaxvy.kunirx.todo.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.UIActionHandler
import com.jaxvy.kunirx.UIState
import com.jaxvy.kunirx.UIViewActivity
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.di.IOScheduler
import com.jaxvy.kunirx.todo.di.MainScheduler
import com.jaxvy.kunirx.todo.di.inject
import dagger.Reusable
import io.reactivex.Observable
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_todo.*
import javax.inject.Inject

class TodoActivityUIState(
    val todo: List<Todo>
) : UIState

@Reusable
class TodoActivityUIActionConfig @Inject constructor(
    @MainScheduler override var mainScheduler: Scheduler,
    @IOScheduler override var ioScheduler: Scheduler
) : UIActionHandler.Configuration()


class TodoActivity : UIViewActivity<TodoActivityUIState>() {

    private lateinit var todoAdapter: TodoAdapter

    @Inject
    lateinit var uiActionConfig: TodoActivityUIActionConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        uiState = TodoActivityUIState(
            todo = (1..10).map { Todo(it.toString()) }
        )

        todoAdapter = TodoAdapter()
        todoRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@TodoActivity)
            adapter = todoAdapter
        }
    }

    override fun uiActionHandlerConfiguration(): UIActionHandler.Configuration = uiActionConfig

    override fun uiActionInputObservable(): Observable<UIAction.Input> = Observable.empty()

    override fun render(uiState: TodoActivityUIState) {
        todoAdapter.update(
            todos = uiState.todo
        )
    }
}
