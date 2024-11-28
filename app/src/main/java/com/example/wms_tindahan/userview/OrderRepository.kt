package com.example.wms_tindahan.userview

import android.content.Context
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.NewOrderRequest
import com.example.wms_tindahan.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository(
    private val context: Context
    ) {
        private val apiService = RetrofitClient.getInstance(context)

    fun postOrder(newOrder: NewOrderRequest, onSuccess: (List<CartItem>) -> Unit, onError: (String) -> Unit) {
        apiService.placeOrder(newOrder).enqueue(object : Callback<List<CartItem>> {
            override fun onResponse(call: Call<List<CartItem>>, response: Response<List<CartItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<CartItem>>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }


}