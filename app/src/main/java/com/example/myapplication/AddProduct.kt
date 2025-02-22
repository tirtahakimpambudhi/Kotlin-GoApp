package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentAddProductBinding
import com.example.myapplication.entities.Product
import com.example.myapplication.view.model.ProductViewModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddProduct(private var productItem: Product? = null) : Fragment() {
    private var READ_EXTERNAL_STORAGE_PERMISSION_CODE: Int = 2
    private var READ_MEDIA_IMAGES_PERM_CODE :Int = 6
    private val PICK_IMAGE_REQUEST = 100
    private var currentPhotoPath: String? = null

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productViewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProvider(requireActivity())[ProductViewModel::class.java]
        setupToolbar()
        setupCategorySpinner()
        setupSaveButton()
        // If productItem exists, populate fields for editing
        productItem?.let { product ->
            binding.apply {
                tvTitle.text = getString(R.string.edit_product)
                etProductName.setText(product.name)
                etDescription.setText(product.description)
                etPrice.setText(product.price.toString())
                etStock.setText(product.stock.toString())
                ivProduct.setImageBitmap(BitmapFactory.decodeFile(product.image))
                val categories = arrayOf("Electronics", "Clothing", "Books", "Food", "Other")
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
                spinnerCategory.setAdapter(adapter)
            }
        }

        // Setup back navigation
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // Setup image selection
        binding.cardImage.setOnClickListener {
            checkAndRequestPermissions()
        }

        binding.toolbar.setOnClickListener {
            val intent = Intent(requireContext(), ProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED -> {
                    openImagePicker()
                }
                else -> {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        READ_MEDIA_IMAGES_PERM_CODE
                    )
                }
            }
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    openImagePicker()
                }
                else -> {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        READ_EXTERNAL_STORAGE_PERMISSION_CODE
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_CODE, READ_MEDIA_IMAGES_PERM_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImagePicker()
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                copyImageToAppStorage(uri)
            }
        }
    }

    private fun copyImageToAppStorage(uri: Uri) {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "PRODUCT_${timeStamp}.jpg"

            // Get app's external files directory
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val destinationFile = File(storageDir, imageFileName)

            // Copy the file
            requireContext().contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }

            // Save the file path and update the image view
            currentPhotoPath = destinationFile.absolutePath
            updateProductImage(currentPhotoPath!!)

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProductImage(imagePath: String) {
        // Hide the add photo icon
        binding.ivAddPhoto.visibility = View.GONE

        // Load and display the image
        binding.ivProduct.setImageBitmap(BitmapFactory.decodeFile(imagePath))
    }


    private fun setupToolbar() {
        binding.tvTitle.text = if (productItem == null) {
            getString(R.string.add_product)
        } else {
            getString(R.string.edit_product)
        }
    }

    private fun setupCategorySpinner() {
        val categories = arrayOf("Electronics", "Clothing", "Books", "Food", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.spinnerCategory.setAdapter(adapter)
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val name = binding.etProductName.text.toString()
            val description = binding.etDescription.text.toString()
            val priceText = binding.etPrice.text.toString()
            val stockText = binding.etStock.text.toString()
            val category = binding.spinnerCategory.text.toString()

            if (validateInputs(name, description, priceText, stockText, category)) {
                val price = priceText.toDouble()
                val stock = stockText.toInt()

                if (productItem == null) {
                    val product = Product(
                        0,
                        name,
                        price,
                        stock,
                        currentPhotoPath ?: "", // Use the saved image path
                        description,
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                    )
                    productViewModel.addProductItem(product)
                } else {
                    productItem!!.apply {
                        this.name = name
                        this.price = price
                        this.stock = stock
                        this.description = description
                        this.image = currentPhotoPath ?: this.image // Update image path if new image selected
                    }
                    productViewModel.updateProductItem(
                        productId = productItem!!.productId,
                        productItem!!
                    )
                }

                requireActivity().onBackPressed()
            }
        }
    }

    private fun validateInputs(
        name: String,
        description: String,
        price: String,
        stock: String,
        category: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.tilProductName.error = "Product name is required"
            isValid = false
        }

        if (description.isEmpty()) {
            binding.tilDescription.error = "Description is required"
            isValid = false
        }

        if (price.isEmpty()) {
            binding.tilPrice.error = "Price is required"
            isValid = false
        }

        if (stock.isEmpty()) {
            binding.tilStock.error = "Stock is required"
            isValid = false
        }

        if (category.isEmpty()) {
            binding.tilCategory.error = "Category is required"
            isValid = false
        }

        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}