package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ProductItemBinding
import com.example.myapplication.entities.Product
import com.example.myapplication.listener.ProductClickListener
import com.example.myapplication.view.holder.ProductViewHolder

class ProductAdapter(
    private val productItems: List<Product>,
    private val clickListener: ProductClickListener
): RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = ProductItemBinding.inflate(from, parent, false)
        return ProductViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int = productItems.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindProduct(productItems[position])
    }
}