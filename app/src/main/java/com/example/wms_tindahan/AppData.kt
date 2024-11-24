package com.example.wms_tindahan

data class User(
    val email: String,
    val name: String,
    val isAdmin: Int,
)

data class Item (
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String
)

data class NewItemRequest(
    val user_id: Int = 2,
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String = "dorito.png"
    )

data class RegisterRequest(val email: String, val name: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class ApiResponse(val message: String, val id: Int?)