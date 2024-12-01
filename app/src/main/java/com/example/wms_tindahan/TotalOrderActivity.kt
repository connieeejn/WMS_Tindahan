package com.example.wms_tindahan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalOrderActivity : AppCompatActivity() {

    private lateinit var rvTransactions: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_order)

        rvTransactions = findViewById(R.id.rvTransactions)
        rvTransactions.layoutManager = LinearLayoutManager(this)

        // Get transactions from API and set up adapter
        fetchTransactions()
    }

    private fun fetchTransactions() {
        val apiService = RetrofitClient.getInstance(this)

        // Asynchronous API call using enqueue
        apiService.getAllTransactions().enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(call: Call<List<Transaction>>, response: Response<List<Transaction>>) {
                if (response.isSuccessful && response.body() != null) {
                    val transactions = response.body() ?: emptyList()

                    // Set up the adapter after data is fetched
                    adapter = TransactionAdapter(transactions) { transaction ->
                        // Handle item click and open order details

                    }
                    rvTransactions.adapter = adapter
                } else {
                    Log.e("FETCH_TRANSACTIONS_ERROR", "Failed to fetch transactions: ${response.errorBody()?.string()}")
                    Toast.makeText(this@TotalOrderActivity, "Error fetching transactions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                Log.e("FETCH_TRANSACTIONS_ERROR", "Error fetching transactions: ${t.message}")
                Toast.makeText(this@TotalOrderActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
