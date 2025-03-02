package com.example.myapplication.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
// ISSUE
data class ProductWithCategories(
    @Embedded var product: Product,
    @Relation(
        parentColumn = "productId",
        entityColumn = "categoryId",
        associateBy = Junction(ProductCategoryCrossRef::class)
    )
    var categories: List<Category>
)