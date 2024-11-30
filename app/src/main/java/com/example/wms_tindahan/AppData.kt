package com.example.wms_tindahan

data class User(
    val email: String,
    val name: String,
    var isAdmin: Int,
    val _id: String? ,
    val id:Int
)
data class Item (
    val user_id: Int = 1,
    val id: Int? = null,
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String
)

data class NewItemRequest(
    val user_id: Int = 1,
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String
    )

data class Transaction(
    val transaction_id: Int,
    val user_id: Int,
    val items: List<OrderItem>,
    val total_order_price: Double,
    val _id: String? ,
    val order_date:String
)

data class OrderItem(
    val item_id: Int,
    val item_name: String,
    val price: Double,
    val quantity: Int,
    val total_price: Double
)

data class RegisterRequest(val email: String, val name: String, val password: String)
data class LoginRequest(val email: String, val password: String)
data class ApiResponse(val message: String, val id: Int?)
data class TopSellingItem(
    val _id: Int,
    val item_name: String,
    val total_quantity: Int
)