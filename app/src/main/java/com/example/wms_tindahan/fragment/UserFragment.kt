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
import com.example.wms_tindahan.R
import com.example.wms_tindahan.RetrofitClient
import com.example.wms_tindahan.User

import com.example.wms_tindahan.UserAdapter // Create or modify your adapter to display all users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class UserFragment : Fragment() {

    private lateinit var totalUsersValue: TextView
    private lateinit var adminCountValue: TextView
    private lateinit var recyclerViewAllUsers: RecyclerView
    private lateinit var usersAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        // Initialize UI Components
        totalUsersValue = view.findViewById(R.id.totalUsersValue)
        adminCountValue = view.findViewById(R.id.totalAdminsValue)
        recyclerViewAllUsers = view.findViewById(R.id.userListView)

        // Setup RecyclerView
        recyclerViewAllUsers.layoutManager = LinearLayoutManager(requireContext())
        usersAdapter = UserAdapter(mutableListOf(), requireContext(), this)
        recyclerViewAllUsers.adapter = usersAdapter

        // Fetch Users
        fetchUsers()

        return view
    }

     fun fetchUsers() {
        val baseUrl = getBaseUrlFromAssets()

        if (baseUrl.isEmpty()) {
            Toast.makeText(requireContext(), "Base URL not found. Please check configuration.", Toast.LENGTH_LONG).show()
            return
        }

        val apiService = RetrofitClient.getInstance(requireContext())

        apiService.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!
                    displayUserCounts(users)
                    loadAllUsers(users)
                } else {
                    handleErrorResponse("Failed to fetch users", response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                handleFailure("Error fetching users", t)
            }
        })
    }

    fun displayUserCounts(users: List<User>) {
        val totalUsers = users.size
        val adminCount = users.count { it.isAdmin == 1 }
        // Display counts
        totalUsersValue.text = totalUsers.toString()
        adminCountValue.text = adminCount.toString()

        Log.d("USER_COUNTS", "Total users: $totalUsers, Admin count: $adminCount")
    }

    private fun loadAllUsers(users: List<User>) {
        // Update RecyclerView with all users
        usersAdapter.updateUsers(users)
        Log.d("ALL_USERS_LOADED", "All users loaded into RecyclerView.")
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

    private fun handleErrorResponse(logMessage: String, errorBody: String?) {
        Log.e("API_ERROR", "$logMessage: $errorBody")
        Toast.makeText(requireContext(), "Error: $logMessage", Toast.LENGTH_SHORT).show()
    }

    private fun handleFailure(logMessage: String, t: Throwable) {
        Log.e("API_FAILURE", "$logMessage: ${t.message}")
        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
    }
    fun refreshUserList(users: List<User>) {
        usersAdapter.updateUsers(users)
    }
}
