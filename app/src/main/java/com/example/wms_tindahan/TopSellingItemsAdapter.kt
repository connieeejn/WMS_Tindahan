package com.example.wms_tindahan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TopSellingItemsAdapter(private var items: List<TopSellingItem>) : RecyclerView.Adapter<TopSellingItemsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val itemName: TextView = view.findViewById(R.id.itemTopSellingName)
        val itemQuantity: TextView = view.findViewById(R.id.itemSoldQuantity)
        val itemImage: ImageView = view.findViewById(R.id.itemTopSellingImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topselling, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.item_name
        holder.itemQuantity.text = "Quantity Sold: ${item.total_quantity}"

        // Dynamically set the image based on position
        when (position) {
            0 -> holder.itemImage.setImageResource(R.drawable.gold)   // First item - Gold
            1 -> holder.itemImage.setImageResource(R.drawable.silver) // Second item - Silver
            2 -> holder.itemImage.setImageResource(R.drawable.bronze) // Third item - Bronze

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Method to update the items in the adapter
    fun updateItems(newItems: List<TopSellingItem>) {
        items = newItems
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }
}
