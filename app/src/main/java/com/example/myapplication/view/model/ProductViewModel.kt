package com.example.myapplication.view.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.entities.Product
import java.time.LocalDate
import java.time.LocalDateTime

class ProductViewModel : ViewModel() {
    var productItems = MutableLiveData<MutableList<Product>?>()

    init {
        productItems.value = mutableListOf()
    }

    fun addProductItem(item: Product) {
        val list = productItems.value;
        list!!.add(item)
        productItems.postValue(list)
    }


    fun updateProductItem(productId: Int, item: Product ) {
        val list = productItems.value
        val productItem = list!!.find { it.productId == productId }!!
        productItem.name = item.name
        productItem.image = item.image
        productItem.price = item.price
        productItem.description = item.description
        productItem.stock = item.stock
        productItem.modifiedAt = System.currentTimeMillis()
        productItems.postValue(list)
    }


    fun deleteProduct(id: Int)
    {
        val list = productItems.value
        val todo = list!!.find { it.productId == id }!!
        list.remove(todo)
        productItems.postValue(list)
    }
}