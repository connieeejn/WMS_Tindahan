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
    val user_id: Int = 0, // required for deleting/updating an item
    val id: Int? = null,
    val item_name: String,
    val item_description: String,
    val category: String,
    val price: Double,
    val stock_quantity: Int,
    val image: String
) : Parcelable


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
    val _id: String? ,
    val transaction_id: Int,
    val user_id: Int,
    val items: List<OrderItem>,
    val total_order_price: Double,
    val order_date:String
)

@Parcelize
data class OrderItem(
    val item_id: Int,
    val item_name: String,
    val price: Double,
    var quantity: Int? = null,
    var total_price:Double?=null
): Parcelable

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

data class ApiResponse(val message: String, val id: Int?, val user: User, val email: String, val total_order_price: Double?, val transaction_id: Int?)

data class TopSellingItem(
    val _id: Int,
    val item_name: String,
    val total_quantity: Int)

data class ErrorResponse(
    val error: String
)


