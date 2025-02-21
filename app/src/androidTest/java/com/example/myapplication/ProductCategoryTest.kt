package com.example.myapplication

import android.content.Context
//import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.dao.ProductCategoryDao
import com.example.myapplication.databases.AppDatabase
import com.example.myapplication.entities.Category
import com.example.myapplication.entities.Product
import com.example.myapplication.entities.ProductCategoryCrossRef
import com.example.myapplication.entities.ProductWithCategories
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException




@RunWith(AndroidJUnit4::class)
class ProductCategoryTest {
    private lateinit var productCategoryDao: ProductCategoryDao
    private lateinit var db: AppDatabase
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        productCategoryDao = db.productCategoryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
    @Test
    fun insert() = runBlocking {
        try {
            val product = Product(0,"Test Product", 10.0, 10,"image.png", "description", System.currentTimeMillis(), System.currentTimeMillis())
            val category = Category(0,"Test Category")
            productCategoryDao.insertProduct(product)
            productCategoryDao.insertCategory(category)

            productCategoryDao.insertProductCategoryCrossRef(crossRef = ProductCategoryCrossRef(productId = 1, categoryId = 1))
            val productWithCategories: List<ProductWithCategories> = productCategoryDao.getProductWithCategories(productId = 1)
            TestCase.assertNotNull(productWithCategories)

        } catch (error :Exception) {
            TestCase.assertNull(error)

        }
    }
}