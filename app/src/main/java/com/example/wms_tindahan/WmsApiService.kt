package com.example.wms_tindahan

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WmsApiService {

//    // register a new user
//    @POST("api/register")
//    fun register(@Body user: User): Call<User>

    @POST("/api/register")
    fun registerUser(@Body request: RegisterRequest): Call<ApiResponse>

    @POST("/api/login") // Add login route in your Flask API
    fun loginUser(@Body request: LoginRequest): Call<ApiResponse>

    // get users
    @GET("api/users")
    fun getUsers() : Call<List<User>>

    @GET("/api/items")
    fun getAllItems(): Call<List<Item>>

    @POST("/api/item")
    fun addItem(@Body newItem: NewItemRequest): Call<Item>

    @DELETE("/api/item/{id}")
    fun deleteItem(@Path("id") itemId: Int): Call<Void>

    @POST("/api/order")
    fun placeOrder(@Body newOrder: NewOrderRequest): Call<List<CartItem>>
}