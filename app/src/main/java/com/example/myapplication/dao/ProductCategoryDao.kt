package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myapplication.entities.Category
import com.example.myapplication.entities.CategoryWithProducts
import com.example.myapplication.entities.ProductCategoryCrossRef
import com.example.myapplication.entities.Product
import com.example.myapplication.entities.ProductWithCategories

@Dao
interface ProductCategoryDao {
    @Insert
    suspend fun insertProduct(product: Product)

    @Insert
    suspend fun insertCategory(category: Category)

    @Insert
    suspend fun insertProductCategoryCrossRef(crossRef: ProductCategoryCrossRef)

    @Transaction
    @Query("""
        SELECT * FROM products 
        INNER JOIN products_categories_cross_ref ON products.productId = products_categories_cross_ref.productId 
        INNER JOIN categories ON products_categories_cross_ref.categoryId = categories.categoryId 
        WHERE products.productId = :productId
    """)
    suspend fun getProductWithCategories(productId: Int): List<ProductWithCategories>

    @Transaction
    @Query("""
        SELECT * FROM categories 
        INNER JOIN products_categories_cross_ref ON categories.categoryId = products_categories_cross_ref.categoryId 
        INNER JOIN products ON products_categories_cross_ref.productId = products.productId 
        WHERE categories.categoryId = :categoryId
    """)
    suspend fun getCategoryWithProducts(categoryId: Int): List<CategoryWithProducts>
}
