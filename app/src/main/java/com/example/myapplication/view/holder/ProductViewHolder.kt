package com.example.myapplication.view.holder

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AlertDialog
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

    fun bindProduct(item: Product) {
        binding.tvProductName.text = item.name
        binding.tvPrice.text = item.price.toString()
        binding.tvStock.text = item.stock.toString()
        binding.ivProduct.setImageBitmap(BitmapFactory.decodeFile(item.image))

        binding.menuButton.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Product Action")
            dialog.setMessage("Do you want to proceed with this action?")

            dialog.setPositiveButton("Edit") { dialog, _ ->
                clickListener.editProductItem(item)
            }

            dialog.setNegativeButton("Delete") { dialog, _ ->
                clickListener.deleteProductItem(item)
            }
            dialog.create().show()
        }
    }
}