package com.example.myapplication.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CategoryWithProducts(
    @Embedded var category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "productId",
        associateBy = Junction(ProductCategoryCrossRef::class)
    )
    var products: List<Product>
)