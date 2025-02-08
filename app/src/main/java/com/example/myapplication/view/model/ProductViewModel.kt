package com.example.myapplication.view.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.entities.Product

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


    fun deleteTodoItem(id: Int)
    {
        val list = productItems.value
        val todo = list!!.find { it.productId == id }!!
        list.remove(todo)
        productItems.postValue(list)
    }


}