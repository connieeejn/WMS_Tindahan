package com.example.userview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.wms_tindahan.CartItem
import com.example.wms_tindahan.R
import java.lang.Exception
import java.util.concurrent.Executors

class ItemUserAdapter(private val cartItems: MutableList<CartItem>):
    RecyclerView.Adapter<ItemUserAdapter.UserItemViewHolder>(){

    inner class UserItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.itemName)
        val description: TextView = view.findViewById(R.id.itemDescription)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val quantity: TextView = view.findViewById((R.id.quantityTextView))
        val subtractButton: AppCompatButton = view.findViewById(R.id.subtractButton)
        val addButton: AppCompatButton = view.findViewById(R.id.addButton)
        val image: ImageView = view.findViewById(R.id.itemImg)

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

        // load image
        val productImgView: ImageView = holder.image
        loadImage(productImgView, product.image)

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

    private fun loadImage(imgView: ImageView, productImgUrl: String) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null;

        if(productImgUrl.isNotEmpty()) {
            executor.execute{
                try {
                    val `in` = java.net.URL(productImgUrl).openStream()

                    image = BitmapFactory.decodeStream(`in`)

                    handler.post {
                        imgView.setImageBitmap(image)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            imgView.setImageResource(R.drawable.profile_foreground)
        }
    }
}