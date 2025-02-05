package com.example.myapplication.listener

import com.example.myapplication.entities.Product

interface ProductClickListener {
    fun addProductItem(item: Product)
    fun editProductItem(item: Product)
    fun deleteProductItem(item: Product)
}