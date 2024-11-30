package com.example.userview

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.NewOrderRequest
import com.example.wms_tindahan.R
import com.example.wms_tindahan.Transaction

class OrderUserAdapter(private val cartItemList: List<CartItem>):
    RecyclerView.Adapter<OrderUserAdapter.OrderViewHolder>(){

    // TODO: add image
    // val image: ImageView = view.findViewById(R.id.itemImage)

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.itemName)
        val totalItemPrice: TextView = view.findViewById(R.id.totalItemPrice)
        val quantity: TextView = view.findViewById((R.id.itemQty))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        // Inflate the layout for each item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_user_card, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val cartList = cartItemList[position]
        val item = cartList.item

        // Set the item details to the views
        holder.name.text = item.item_name
        holder.totalItemPrice.text = "$${item.price}"
        holder.quantity.text = cartList.quantity.toString()


    }


    override fun getItemCount(): Int {
        return cartItemList.size
    }

    /*fun getItemsWithQuantityGreaterThanZero(): List<CartItem> {
        return cartItems.filter { it.quantity > 0 }
    }*/
}