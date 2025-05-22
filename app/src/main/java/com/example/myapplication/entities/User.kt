package com.example.myapplication.entities


data class User(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = ""
) {
    constructor() : this("", "", "", "")

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "phone" to phone
        )
    }
}