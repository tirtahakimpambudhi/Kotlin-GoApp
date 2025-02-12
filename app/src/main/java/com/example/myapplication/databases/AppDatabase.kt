package com.example.myapplication.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.dao.CategoryDao
import com.example.myapplication.dao.ProductCategoryDao
import com.example.myapplication.dao.ProductDao
import com.example.myapplication.entities.Category
import com.example.myapplication.entities.ProductCategoryCrossRef
import com.example.myapplication.entities.Product

@Database(entities = [Product::class,Category::class,ProductCategoryCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao() :ProductDao
    abstract fun categoryDao() :CategoryDao
    abstract fun productCategoryDao() :ProductCategoryDao
}