package com.example.userview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.R

class ItemUserAdapter(private val cartItems: MutableList<CartItem>):
    RecyclerView.Adapter<ItemUserAdapter.UserItemViewHolder>(){

    // TODO: add image
    // val image: ImageView = view.findViewById(R.id.itemImage)

    inner class UserItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.itemName)
        val description: TextView = view.findViewById(R.id.itemDescription)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val quantity: TextView = view.findViewById((R.id.quantityTextView))
        val subtractButton: AppCompatButton = view.findViewById(R.id.subtractButton)
        val addButton: AppCompatButton = view.findViewById(R.id.addButton)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserItemViewHolder {
        // Inflate the layout for each item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_card, parent, false)
        return UserItemViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val cartItem = cartItems[position]
        val product = cartItem.item

        // Set the item details to the views
        holder.name.text = product.item_name
        holder.description.text = product.item_description
        holder.price.text = "$${product.price}"

        // Set initial quantity
        holder.quantity.text = cartItem.quantity.toString()

        holder.subtractButton.setOnClickListener {
            if (cartItem.quantity > 0) { // Ensure quantity doesn't go below 0
                cartItem.quantity--
                holder.quantity.text = cartItem.quantity.toString()
                notifyItemChanged(position)  // Notify that this item has changed
            }
        }

      holder.addButton.setOnClickListener {
            cartItem.quantity++
            holder.quantity.text = cartItem.quantity.toString()
            notifyItemChanged(position)  // Notify that this item has changed

        }

        }


    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun getItemsWithQuantityGreaterThanZero(): List<CartItem> {
        return cartItems.filter { it.quantity > 0 }
    }
}