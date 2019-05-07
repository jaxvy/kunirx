package com.jaxvy.kunirx.todo.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.widget.checkedChanges
import com.jaxvy.kunirx.UIAction
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.model.Todo
import com.jaxvy.kunirx.todo.ui.list.action.UpdateTodoCheckmarkUIAction
import com.jaxvy.kunirx.todo.utils.KunirxAdapter
import com.jaxvy.kunirx.todo.utils.KunirxViewHolder
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.adapter_item_todo.*

class TodoListAdapter : KunirxAdapter<TodoListAdapter.TodoViewHolder>() {

    private var todos: List<Todo> = emptyList()

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
        )
    }

    override fun getItemCount(): Int = todos.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    class TodoViewHolder(
        override val containerView: View
    ) : KunirxViewHolder(containerView), LayoutContainer {

        private lateinit var todo: Todo

        override fun uiEvents(): Observable<UIAction.Input> {
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