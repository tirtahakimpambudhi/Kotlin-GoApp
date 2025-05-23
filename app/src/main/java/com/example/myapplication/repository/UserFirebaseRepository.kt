package com.example.myapplication.repository

import com.example.myapplication.entities.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.channels.awaitClose


class UserFirebaseRepository {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    suspend fun addUser(user: User): Result<User> {
        return try {
            val newUserRef = usersRef.push()
            user.id = newUserRef.key ?: ""
            newUserRef.setValue(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getAllUsers(): Flow<Result<List<User>>> = callbackFlow {
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                for (childSnapshot in snapshot.children) {
                    val user = childSnapshot.getValue(User::class.java)
                    user?.let { userList.add(it) }
                }
                trySend(Result.success(userList))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        usersRef.addValueEventListener(userListener)

        // Remove the listener when Flow collection ends
        awaitClose {
            usersRef.removeEventListener(userListener)
        }
    }

    suspend fun getUserById(userId: String): Result<User> {
        return try {
            val dataSnapshot = usersRef.child(userId).get().await()
            val user = dataSnapshot.getValue(User::class.java)
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(user: User): Result<User> {
        return try {
            if (user.id.isEmpty()) {
                return Result.failure(Exception("User ID Invalid"))
            }

            usersRef.child(user.id).setValue(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: String): Result<Boolean> {
        return try {
            usersRef.child(userId).removeValue().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}