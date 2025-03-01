package com.example.myapplication.entities

import androidx.room.Entity

@Entity(primaryKeys = ["productId","categoryId"], tableName = "products_categories_cross_ref")
data class ProductCategoryCrossRef(
    var productId  :Int,
    var categoryId :Int
)
