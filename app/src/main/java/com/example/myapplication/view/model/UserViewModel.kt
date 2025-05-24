package com.example.myapplication.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.entities.User
import com.example.myapplication.repository.UserFirebaseRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserFirebaseRepository()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            repository.getAllUsers().collect { result ->
                result.onSuccess { userList ->
                    _users.value = userList
                }
                result.onFailure { exception ->
                    _status.value = "Error: ${exception.message}"
                }
            }
        }
    }

    fun addUser(name: String, email: String, phone: String) {
        val newUser = User(name = name, email = email, phone = phone)

        viewModelScope.launch {
            repository.addUser(newUser)
                .onSuccess {
                    _status.value = "User successfully registered"
                    // Reload user list after adding a new user
                    loadUsers()
                }
                .onFailure { exception ->
                    _status.value = "Failed to registered User: ${exception.message}"
                }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
                .onSuccess {
                    _status.value = "User successfully updated"
                    _currentUser.value = User() // Reset form

                    // Update the local list immediately
                    updateLocalUsersList(user)

                    // Also reload from the server to ensure consistency
                    loadUsers()
                }
                .onFailure { exception ->
                    _status.value = "Failed updated User: ${exception.message}"
                }
        }
    }

    // Helper method to update the local users list
    private fun updateLocalUsersList(updatedUser: User) {
        val currentList = _users.value?.toMutableList() ?: mutableListOf()
        val index = currentList.indexOfFirst { it.id == updatedUser.id }

        if (index != -1) {
            // Replace the old user with the updated one
            currentList[index] = updatedUser
            _users.value = currentList
        }
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            repository.deleteUser(userId)
                .onSuccess {
                    _status.value = "User successfully deleted"

                    // Remove from local list immediately
                    removeUserFromLocalList(userId)

                    // Also reload from server
                    loadUsers()
                }
                .onFailure { exception ->
                    _status.value = "Failed deleted User: ${exception.message}"
                }
        }
    }

    // Helper method to remove a user from the local list
    private fun removeUserFromLocalList(userId: String) {
        val currentList = _users.value?.toMutableList() ?: mutableListOf()
        val updatedList = currentList.filter { it.id != userId }
        _users.value = updatedList
    }

    fun getUserById(userId: String) {
        viewModelScope.launch {
            repository.getUserById(userId)
                .onSuccess { user ->
                    _currentUser.value = user
                }
                .onFailure { exception ->
                    _status.value = "Failed get User: ${exception.message}"
                }
        }
    }

    fun setUserForEdit(user: User) {
        _currentUser.value = user
    }

    fun resetForm() {
        _currentUser.value = User()
    }

    fun clearCurrentUser() {
        _currentUser.value = User("", "", "", "")
    }
}