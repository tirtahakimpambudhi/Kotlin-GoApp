package com.example.myapplication.view.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.entities.TodoEntity
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class TodoViewModel: ViewModel()
{
    var todoItem = MutableLiveData<MutableList<TodoEntity>>()

    init {
        todoItem.value = mutableListOf()
    }

    fun addTodoItem(newTodo: TodoEntity)
    {
        val list = todoItem.value
        list!!.add(newTodo)
        todoItem.postValue(list)
    }

    fun updateTodoItem(id: UUID, name: String, desc: String, dueTime: LocalTime?)
    {
        val list = todoItem.value
        val todo = list!!.find { it.id == id }!!
        todo.name = name
        todo.desc = desc
        todo.dueTime = dueTime
        todoItem.postValue(list)
    }

    fun deleteTodoItem(id: UUID)
    {
        val list = todoItem.value
        val todo = list!!.find { it.id == id }!!
        list.remove(todo)
        todoItem.postValue(list)
    }

    fun setCompleted(todoItemParam: TodoEntity)
    {
        val list = todoItem.value
        val todo = list!!.find { it.id == todoItemParam.id }!!
        if (todo.completedDate == null)
            todo.completedDate = LocalDate.now()
        todoItem.postValue(list)
    }

    fun unsetCompleted(todoItemParam: TodoEntity)
    {
        val list = todoItem.value
        val todo = list!!.find { it.id == todoItemParam.id }!!
        if (todo.completedDate != null)
            todo.completedDate = null
        todoItem.postValue(list)
    }
}