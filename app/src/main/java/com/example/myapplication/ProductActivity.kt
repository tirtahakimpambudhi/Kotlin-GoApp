package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.databinding.ActivityProductBinding
import com.example.myapplication.entities.Product
import com.example.myapplication.listener.ProductClickListener
import com.example.myapplication.view.model.ProductViewModel

class ProductActivity : AppCompatActivity(), ProductClickListener {
    private lateinit var binding: ActivityProductBinding
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        binding.fabAddProduct.setOnClickListener {
            showAddProductFragment(null)
        }

        setRecyclerView()
    }

    private fun showAddProductFragment(product: Product?) {
        val fragment = AddProduct(product)
        binding.fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            binding.fragmentContainer.visibility = View.GONE
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    private fun setRecyclerView() {
        val mainActivity = this
        productViewModel.productItems.observe(this) {
            binding.rvProducts.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = it?.let { it1 -> ProductAdapter(it1, mainActivity) }
            }
        }
    }

    override fun addProductItem(item: Product) {
        showAddProductFragment(null)
    }

    override fun editProductItem(item: Product) {
        showAddProductFragment(item)
    }

    override fun deleteProductItem(item: Product) {
        productViewModel.deleteProduct(item.productId)
    }
}