package com.example.myapplication.listener

import com.example.myapplication.entities.TodoEntity

interface TodoClickListener
{
    fun editTodoItem(todoItem: TodoEntity)
    fun completeTodoItem(todoItem: TodoEntity)
    fun unCompleteTodoItem(todoItem: TodoEntity)
    fun deleteTodoItem(todoItem: TodoEntity)
}