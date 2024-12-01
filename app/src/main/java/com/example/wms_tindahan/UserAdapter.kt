
package com.example.wms_tindahan

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.wms_tindahan.fragment.UserFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAdapter(
    private var items: MutableList<User>,
    private val context: Context,
    private val userFragment: UserFragment,

) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val apiService = RetrofitClient.getInstance(context)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userID: TextView = view.findViewById(R.id.userID)
        val userName: TextView = view.findViewById(R.id.userName)
        val userEmail: TextView = view.findViewById(R.id.userEmail)
        val userRole: CheckBox = view.findViewById(R.id.userRole)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = items[position]
        holder.userID.text = user.id.toString()
        holder.userName.text = user.name
        holder.userEmail.text = user.email

        // Set initial checkbox state
        holder.userRole.setOnCheckedChangeListener(null) // Clear previous listener
        holder.userRole.isChecked = user.isAdmin == 1

        // Set checkbox listener with confirmation dialog
        holder.userRole.setOnCheckedChangeListener { _, isChecked ->
            showConfirmationDialog(holder, user, isChecked)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateUsers(newItems: List<User>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun showConfirmationDialog(holder: ViewHolder, user: User, isChecked: Boolean) {
        val message = if (isChecked) {
            "Do you want to grant admin access to ${user.name}?"
        } else {
            "Do you want to revoke admin access for ${user.name}?"
        }

        AlertDialog.Builder(context)
            .setTitle("Confirm Action")
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ ->
                if (isChecked) {
                    setAdmin(user)  // Pass user directly, apiService is already available at class level
                } else {
                    unsetAdmin(user)  // Pass user directly
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                // Revert checkbox state if action is canceled
                holder.userRole.setOnCheckedChangeListener(null)
                holder.userRole.isChecked = !isChecked
                holder.userRole.setOnCheckedChangeListener { _, checked ->
                    showConfirmationDialog(holder, user, checked)  // Re-open dialog if needed
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun setAdmin(user: User) {
        apiService.setAdmin(user.id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    user.isAdmin = 1
                    notifyItemChanged(items.indexOf(user))
                    Toast.makeText(context, "${user.name} is now an admin.", Toast.LENGTH_SHORT).show()

                    userFragment.fetchUsers()
                } else {
                    Log.e("API_FAILURE", "Error: ${response.code()}, ${response.message()}")
                    Toast.makeText(context, "Failed to grant admin access.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun unsetAdmin(user: User) {
        apiService.unsetAdmin(user.id).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    user.isAdmin = 0
                    notifyItemChanged(items.indexOf(user))
                    Toast.makeText(context, "${user.name} is no longer an admin.", Toast.LENGTH_SHORT).show()
                    userFragment.fetchUsers()
                } else {
                    Toast.makeText(context, "Failed to revoke admin access.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("API_FAILURE", "Failure: ${t.localizedMessage}", t)
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }

}
