package com.jaxvy.kunirx.todo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.checkedChanges
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.ui.action.UpdateTodoCheckmarkUIAction
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_todo.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todos: List<Todo> = emptyList()

    private var uiActionInputSubject = PublishSubject.create<UIAction.Input>()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        uiActionInputSubject.onComplete()
    }

    fun update(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_item_todo,
                parent,
                false
            )
        ).also {
            it.uiEvents().subscribe(uiActionInputSubject)
        }
    }

    override fun getItemCount(): Int = todos.size

    fun uiActionInputObservable(): Observable<UIAction.Input> = uiActionInputSubject

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    class TodoViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var todo: Todo

        fun uiEvents(): Observable<UIAction.Input> {
            return todoCheckBox.checkedChanges()
                .skipInitialValue()
                .map { isChecked ->
                    UpdateTodoCheckmarkUIAction.Input(
                        id = todo.id,
                        isChecked = isChecked
                    )
                }
        }

        fun bind(todo: Todo) {
            this.todo = todo
            todoCheckBox.apply {
                text = todo.text
                isChecked = todo.isComplete
            }
        }
    }
}