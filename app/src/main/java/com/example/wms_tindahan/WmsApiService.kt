package com.example.wms_tindahan

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface WmsApiService {

    // register a new user
    @POST("api/register")
    fun register(@Body user: User): Call<User>

    // get users
    @GET("api/users")
    fun getUsers() : Call<List<User>>

    @GET("/api/items")
    fun getAllItems(): Call<List<Item>>

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
}