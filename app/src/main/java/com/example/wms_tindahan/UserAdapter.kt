package com.example.wms_tindahan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private var items: MutableList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

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
        val item = items[position]
        holder.userID.text=item.id.toString()
        holder.userName.text = item.name
        holder.userEmail.text = item.email

        // Set the checkbox value based on the userRole (1 for true, 0 for false)
        holder.userRole.isChecked = item.isAdmin == 1

        // Optional: Handle checkbox click behavior
        holder.userRole.setOnCheckedChangeListener { _, isChecked ->
            // Update the isAdmin field based on checkbox state
            item.isAdmin = if (isChecked) 1 else 0
        }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Method to update the items in the adapter
    fun updateUsers(newItems: List<User>) {
        items.clear() // Clear the existing items
        items.addAll(newItems) // Add the new items
        notifyDataSetChanged() // Notify the adapter about the data changes
    }
}
