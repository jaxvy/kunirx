package com.jaxvy.kunirx.todo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaxvy.kunirx.todo.R
import com.jaxvy.kunirx.todo.model.Todo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.todo_adapter_item.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todos: List<Todo> = emptyList()

    fun update(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.todo_adapter_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = todos.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    class TodoViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(todo: Todo) {
            text.text = todo.text
        }
    }
}