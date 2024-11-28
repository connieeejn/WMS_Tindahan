package com.example.wms_tindahan

data class User(
    val id: Int,
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
    val image: String ="dorito.png" // TODO: remove later when the url image is implemented
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


data class TopSellingItem(
    val _id: Int,
    val item_name: String,
    val total_quantity: Int
=======
data class ApiResponse(val message: String, val id: Int?, val user: User)

data class CartItem(
    val item: Item,    // Original Item (product)
    var quantity: Int,
    var total_item_price: Double
)

data class NewOrderRequest(
    val user_id: Int,
    val items: List<CartItem>,
    var total_order_price: Double,
    val order_date: String


)