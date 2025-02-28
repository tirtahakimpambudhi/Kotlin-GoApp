package com.example.myapplication.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) var productId: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "stock") var stock: Int,
    @ColumnInfo(name = "image") var image: String,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "created_at") var createdAt: Long,
    @ColumnInfo(name = "modified_at") var modifiedAt: Long
)

