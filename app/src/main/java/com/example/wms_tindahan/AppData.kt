package com.example.wms_tindahan

data class User(
    val email: String,
    val name: String,
    val isAdmin: Int,
)

data class Item (
    val user_id: Int = 1,
    val id: Int? = null,
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String ="dorito.png"
)

data class NewItemRequest(
    val user_id: Int = 1,
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String = "dorito.png"
    )

