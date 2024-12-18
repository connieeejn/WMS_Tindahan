package com.example.wms_tindahan

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemRepository(
    private val context: Context
) {
    private val apiService = RetrofitClient.getInstance(context)

    fun getAllItems(onSuccess: (List<Item>) -> Unit, onError: (String) -> Unit) {
        apiService.getAllItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    fun addItem(newItem: NewItemRequest, onSuccess: (Item) -> Unit, onError: (String) -> Unit) {
        apiService.addItem(newItem).enqueue(object : Callback<Item>{
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }

        })
    }

    fun deleteItem(
        itemId: Int,
        userId: Int,  // Accept user_id as a parameter
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        apiService.deleteItem(itemId, userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()  // Item deleted successfully
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }

    // Update an existing item
    fun updateItem(
        itemId: Int,
        updatedItem: Item,  // Updated item data
        onSuccess: (Item) -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.updateItem(itemId, updatedItem).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }


}