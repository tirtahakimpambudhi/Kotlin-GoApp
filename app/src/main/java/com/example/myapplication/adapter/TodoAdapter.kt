package com.example.myapplication.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.TodoItemBinding
import com.example.myapplication.entities.TodoEntity
import com.example.myapplication.listener.TodoClickListener
import com.example.myapplication.view.holder.TodoViewHolder

class TodoAdapter(
    private val todoItem: List<TodoEntity>,
    private val clickListener: TodoClickListener
): RecyclerView.Adapter<TodoViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(from, parent, false)
        return TodoViewHolder(parent.context, binding, clickListener)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bindTodo(todoItem[position])
    }

    override fun getItemCount(): Int = todoItem.size
}