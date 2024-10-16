package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TodoAdapter
import com.example.myapplication.listener.TodoClickListener
import com.example.myapplication.databinding.ActivityTodoListBinding
import com.example.myapplication.entities.TodoEntity
import com.example.myapplication.view.model.TodoViewModel

class TodoListActivity : AppCompatActivity(), TodoClickListener
{
    private lateinit var binding: ActivityTodoListBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        binding.newTaskButton.setOnClickListener {
            AddTodoSheet(null).show(supportFragmentManager, "newTaskTag")
        }
        setRecyclerView()
    }

    private fun setRecyclerView()
    {
        val mainActivity = this
        todoViewModel.todoItem.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TodoAdapter(it, mainActivity)
            }
        }
    }

    override fun editTodoItem(todoItem: TodoEntity)
    {
        AddTodoSheet(todoItem).show(supportFragmentManager,"newTaskTag")
    }

    override fun unCompleteTodoItem(todoItem: TodoEntity) {
        todoViewModel.unsetCompleted(todoItem)
    }

    override fun deleteTodoItem(todoItem: TodoEntity) {
        todoViewModel.deleteTodoItem(todoItem.id)
    }

    override fun completeTodoItem(todoItem: TodoEntity)
    {
        todoViewModel.setCompleted(todoItem)
    }
}







