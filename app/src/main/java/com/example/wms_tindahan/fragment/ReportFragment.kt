package com.example.wms_tindahan.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wms_tindahan.Item
import com.example.wms_tindahan.R
import com.example.wms_tindahan.RetrofitClient
import com.example.wms_tindahan.TopSellingItem
import com.example.wms_tindahan.TopSellingItemsAdapter
import com.example.wms_tindahan.Transaction
import com.example.wms_tindahan.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

class ReportFragment : Fragment() {

    private lateinit var totalCustomersValue: TextView
    private lateinit var totalOrdersValue: TextView
    private lateinit var totalRevenueValue: TextView
    private lateinit var totalInventoryCostsValue: TextView
    private lateinit var recyclerViewTop3Item: RecyclerView
    private lateinit var topSellingItemsAdapter: TopSellingItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        // Initialize UI Components
        totalCustomersValue = view.findViewById(R.id.totalCustomersValue)
        totalOrdersValue = view.findViewById(R.id.totalOrdersValue)
        totalRevenueValue = view.findViewById(R.id.totalRevenueValue)
        totalInventoryCostsValue = view.findViewById(R.id.totalInventoryCostsValue)
        recyclerViewTop3Item = view.findViewById(R.id.recyclerViewTop3Item)

        // Initialize RecyclerView
        recyclerViewTop3Item.layoutManager = LinearLayoutManager(requireContext())
        topSellingItemsAdapter = TopSellingItemsAdapter(emptyList())
        recyclerViewTop3Item.adapter = topSellingItemsAdapter
        recyclerViewTop3Item.setHasFixedSize(true)

        // Load Report Data
        loadReportData()

        return view
    }

    private fun loadReportData() {
        val baseUrl = getBaseUrlFromAssets()

        if (baseUrl.isEmpty()) {
            Toast.makeText(requireContext(), "Base URL not found. Please check configuration.", Toast.LENGTH_LONG).show()
            return
        }

        val apiService = RetrofitClient.getInstance(requireContext())

        // Fetch Total Customers
        apiService.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful && response.body() != null) {
                    val totalUsers = response.body()!!.size
                    totalCustomersValue.text = totalUsers.toString()
                } else {
                    Log.e("FETCH_USERS_ERROR", "Failed to fetch users: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Error fetching users", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("FETCH_USERS_ERROR", "Error fetching users: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch Total Orders and Revenue
        apiService.getAllTransactions().enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(call: Call<List<Transaction>>, response: Response<List<Transaction>>) {
                if (response.isSuccessful && response.body() != null) {
                    val transactions = response.body() ?: emptyList()
                    val transactionSize = transactions.size
                    // Log the transaction size
                    Log.d("TRANSACTIONS_SIZE", "Total transactions: $transactionSize")

                    totalOrdersValue.text = transactions.size.toString()

                    val totalRevenue = transactions.sumOf { it.total_order_price }
                    val totalRevenueFormatted = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalRevenue)
                    totalRevenueValue.text = totalRevenueFormatted
                } else {
                    Log.e("FETCH_TRANSACTIONS_ERROR", "Failed to fetch transactions: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Error fetching transactions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                Log.e("FETCH_TRANSACTIONS_ERROR", "Error fetching transactions: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch Inventory Costs
        apiService.getAllItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful && response.body() != null) {
                    val items = response.body() ?: emptyList()
                    val totalInventoryCost = items.sumOf { it.price * it.stock_quantity }
                    val totalInventoryCostFormatted = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(totalInventoryCost)
                    totalInventoryCostsValue.text = totalInventoryCostFormatted
                } else {
                    Log.e("FETCH_ITEMS_ERROR", "Failed to fetch inventory items: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Error fetching inventory items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Log.e("FETCH_ITEMS_ERROR", "Error fetching inventory items: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Fetch and Display Top Selling Items
        apiService.getTopSellingItems().enqueue(object : Callback<List<TopSellingItem>> {
            override fun onResponse(call: Call<List<TopSellingItem>>, response: Response<List<TopSellingItem>>) {
                if (response.isSuccessful && response.body() != null) {
                    val topSellingItems = response.body()!!
                    topSellingItemsAdapter.updateItems(topSellingItems)
                } else {
                    Log.e("FETCH_TOP_SELLING_ERROR", "Failed to fetch top-selling items: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Error fetching top-selling items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TopSellingItem>>, t: Throwable) {
                Log.e("FETCH_TOP_SELLING_ERROR", "Error fetching top-selling items: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getBaseUrlFromAssets(): String {
        val baseUrl = StringBuilder()
        try {
            val inputStream = requireContext().assets.open("base_url.txt")
            val reader = inputStream.bufferedReader()
            reader.forEachLine { baseUrl.append(it) }
        } catch (e: IOException) {
            Log.e("ReportFragment", "Error reading base URL: ${e.message}")
        }
        return baseUrl.toString()
    }
}
