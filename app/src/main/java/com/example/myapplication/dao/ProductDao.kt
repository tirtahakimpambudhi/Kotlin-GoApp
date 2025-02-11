package com.example.myapplication.dao

import androidx.room.*
import com.example.myapplication.entities.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE productId = :productId")
    suspend fun getById(productId : Int) : Product
    @Query("SELECT * FROM products")
    suspend fun getAll() : List<Product>
    @Insert
    suspend fun create(product : Product)
    @Update
    suspend fun update(product : Product)
    @Delete
    suspend fun delete(product: Product)
}