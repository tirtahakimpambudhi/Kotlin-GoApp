package com.example.myapplication.entities

import androidx.room.Entity

@Entity(primaryKeys = ["productId","categoryId"], tableName = "products_categories_cross_ref")
data class ProductCategoryCrossRef(
    val productId  :Int,
    val categoryId :Int
)
