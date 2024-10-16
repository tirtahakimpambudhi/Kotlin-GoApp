package com.example.myapplication.view.holder

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.TodoItemBinding
import com.example.myapplication.entities.TodoEntity
import com.example.myapplication.listener.TodoClickListener
import java.time.format.DateTimeFormatter

class TodoViewHolder(
    private val context: Context,
    private val binding: TodoItemBinding,
    private val clickListener: TodoClickListener
): RecyclerView.ViewHolder(binding.root)
{
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    fun bindTodo(todoItem: TodoEntity)
    {
        binding.name.text = todoItem.name

        if (todoItem.isCompleted()){
            binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.dueTime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.completeButton.setImageResource(todoItem.imageResource())
        binding.completeButton.setColorFilter(todoItem.imageColor(context))

        binding.completeButton.setOnClickListener{
            if (todoItem.isCompleted()) {
                clickListener.unCompleteTodoItem(todoItem)
                return@setOnClickListener
            }
            clickListener.completeTodoItem(todoItem)
        }

        binding.deleteButton.setOnClickListener{
            clickListener.deleteTodoItem(todoItem);
        }

        binding.taskCellContainer.setOnClickListener{
            clickListener.editTodoItem(todoItem)
        }

        if(todoItem.dueTime != null)
            binding.dueTime.text = timeFormat.format(todoItem.dueTime)
        else
            binding.dueTime.text = ""
    }
}