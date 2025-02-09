package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.Category
import com.example.myapplication.entities.Product

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    suspend fun getById(categoryId : Int) : Category
    @Query("SELECT * FROM categories")
    suspend fun getAll() : List<Category>
    @Insert
    suspend fun create(category : Category)
    @Update
    suspend fun update(category : Category)
    @Delete
    suspend fun delete(category : Category)
}