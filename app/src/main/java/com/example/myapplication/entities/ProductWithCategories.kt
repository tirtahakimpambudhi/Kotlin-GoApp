package com.example.myapplication.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
// ISSUE
data class ProductWithCategories(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "productId",
        entityColumn = "categoryId",
        associateBy = Junction(ProductCategoryCrossRef::class)
    )
    val categories: List<Category>
)