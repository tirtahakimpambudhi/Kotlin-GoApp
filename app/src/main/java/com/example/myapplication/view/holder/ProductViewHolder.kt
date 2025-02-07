package com.example.myapplication.view.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ProductItemBinding
import com.example.myapplication.entities.Product
import com.example.myapplication.listener.ProductClickListener
import java.time.format.DateTimeFormatter

class ProductViewHolder(
    private val context: Context,
    private val binding: ProductItemBinding,
    private val clickListener: ProductClickListener
): RecyclerView.ViewHolder(binding.root) {
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    fun bindTodo(item: Product) {

    }
}