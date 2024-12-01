package com.example.wms_tindahan.userview

import android.content.Context
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.Item
import com.example.wms_tindahan.NewOrderRequest
import com.example.wms_tindahan.RetrofitClient
import com.example.wms_tindahan.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository(
    private val context: Context
    ) {
        private val apiService = RetrofitClient.getInstance(context)

    fun postOrder(newOrder: NewOrderRequest, onSuccess: (NewOrderRequest) -> Unit, onError: (String) -> Unit) {
        apiService.placeOrder(newOrder).enqueue(object : Callback<NewOrderRequest> {
            override fun onResponse(call: Call<NewOrderRequest>, response: Response<NewOrderRequest>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewOrderRequest>, t: Throwable) {
                onError(t.message ?: "Unknown error")
            }
        })
    }




}