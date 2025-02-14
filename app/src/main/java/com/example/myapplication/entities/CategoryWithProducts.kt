package com.example.myapplication.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CategoryWithProducts(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "productId",
        associateBy = Junction(ProductCategoryCrossRef::class)
    )
    val products: List<Product>
)