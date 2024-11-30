package com.example.wms_tindahan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class User(
    val id: Int, // TODO: remove an ID field ??
    val email: String,
    val name: String,
    var isAdmin: Int
    //val _id: String? ,

)

@Parcelize
data class Item (
  //  val user_id: Int = 1, // TODO: remove ??
    val id: Int? = null,
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String ="dorito.png" // TODO: remove later when the url image is implemented
) : Parcelable

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
    val item_id: List<OrderItem>,
    val item_name: String,
    val item_description: String,
    val price: Double,
    var quantity: Int? = null
)

@Parcelize
data class CartItem(
    val item: Item,
    var quantity: Int
): Parcelable

@Parcelize
data class NewOrderRequest(
    val user_id: Int,
    val items: List<CartItem>,
): Parcelable

data class RegisterRequest(val email: String, val name: String, val password: String)
data class LoginRequest(val email: String, val password: String)

data class ApiResponse(val message: String, val id: Int?, val user: User, val email: String)

data class TopSellingItem(
    val _id: Int,
    val item_name: String,
    val total_quantity: Int)




