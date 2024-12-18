package com.example.wms_tindahan

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WmsApiService {

    @POST("/api/register")
    fun registerUser(@Body request: RegisterRequest): Call<ApiResponse>

    @POST("/api/login") // Add login route in your Flask API
    fun loginUser(@Body request: LoginRequest): Call<ApiResponse>

    // get users
    @GET("api/users")
    fun getAllUsers() : Call<List<User>>

    @GET("/api/items")
    fun getAllItems(): Call<List<Item>>

    @Headers("Content-Type: application/json")
    @GET("/api/transactions")
    fun getAllTransactions(): Call<List<Transaction>>

    @POST("/api/item")
    fun addItem(@Body newItem: NewItemRequest): Call<Item>


    @DELETE("/api/item/{id}")
    fun deleteItem(
        @Path("id") itemId: Int,
        @Query("user_id") userId: Int
    ): Call<Void>

    // Update an item
    @PUT("/api/item/{id}")
    fun updateItem(
        @Path("id") itemId: Int,
        @Body updatedItem: Item
    ): Call<Item>

    @GET("/api/top-selling-items")
    fun getTopSellingItems(): Call<List<TopSellingItem>>


    @PUT("api/user/{user_id}/set-admin")
    fun setAdmin(@Path("user_id") userId: Int): Call<ApiResponse>

    @PUT("api/user/{user_id}/unset-admin")
    fun unsetAdmin(@Path("user_id") userId: Int): Call<ApiResponse>

    @POST("/api/order")
    fun placeOrder(@Body newOrder: NewOrderRequest): Call<ApiResponse>


}