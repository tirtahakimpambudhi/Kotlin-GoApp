package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.UserAdapter
import com.example.myapplication.databinding.ActivityUserBinding
import com.example.myapplication.view.model.UserViewModel
import android.view.View


class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupButtons()
        observeViewModel()

        viewModel.loadUsers()

    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            onEditClick = { user ->
                viewModel.setUserForEdit(user)
                binding.formTitleTextView.text = "Edit User"
                binding.saveButton.text = "Update"
            },
            onDeleteClick = { user ->
                viewModel.deleteUser(user.id)
            }
        )

        binding.usersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@UserActivity)
            adapter = userAdapter
        }
    }

    private fun setupButtons() {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Nama dan email harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = viewModel.currentUser.value
            if (currentUser != null && currentUser.id.isNotEmpty()) {
                // Update existing user
                currentUser.name = name
                currentUser.email = email
                currentUser.phone = phone
                viewModel.updateUser(currentUser)
            } else {
                // Add new user
                viewModel.addUser(name, email, phone)
            }

            clearForm()
        }

        binding.resetButton.setOnClickListener {
            clearForm()
        }
    }


    private fun observeViewModel() {
        // Observe user list
        viewModel.users.observe(this) { users ->
            // Log list of users
            android.util.Log.d("FirebaseCRUD", "Received ${users.size} users from ViewModel")
            for (user in users) {
                android.util.Log.d("FirebaseCRUD", "User: ID=${user.id}, Name=${user.name}, Email=${user.email}")
            }

            // Update RecyclerView
            userAdapter.submitList(ArrayList(users))  // Use ArrayList to force list update

            // Update UI based on whether there are users or not
            if (users.isEmpty()) {
                binding.statusTextView?.visibility = View.VISIBLE
            } else {
                binding.statusTextView?.visibility = View.GONE
            }
        }

        // Observe status messages
        viewModel.status.observe(this) { status ->
            if (status.isNotEmpty()) {
                android.util.Log.d("FirebaseCRUD", "Status update: $status")
                binding.statusTextView.text = status
                binding.statusTextView.visibility = View.VISIBLE

                // Hide status message after a delay
                binding.statusTextView.postDelayed({
                    binding.statusTextView.visibility = View.GONE
                    binding.statusTextView.text = ""
                }, 3000)
            }
        }

        // Observe current user for edit
        viewModel.currentUser.observe(this) { user ->
            if (user != null) {
                android.util.Log.d("FirebaseCRUD", "Current user set for editing: ${user.id}")
                binding.nameEditText.setText(user.name)
                binding.emailEditText.setText(user.email)
                binding.phoneEditText.setText(user.phone)

                if (user.id.isNotEmpty()) {
                    binding.formTitleTextView.text = "Edit User"
                    binding.saveButton.text = "Update"
                } else {
                    binding.formTitleTextView.text = "Tambah User Baru"
                    binding.saveButton.text = "Simpan"
                }
            } else {
                clearForm()
            }
        }
    }

    private fun clearForm() {
        binding.nameEditText.text?.clear()
        binding.emailEditText.text?.clear()
        binding.phoneEditText.text?.clear()
        binding.formTitleTextView.text = "Tambah User Baru"
        binding.saveButton.text = "Simpan"
        viewModel.clearCurrentUser()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUsers() // Refresh data when activity resumes
    }
}