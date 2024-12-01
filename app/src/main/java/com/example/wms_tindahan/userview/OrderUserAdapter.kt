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

class OrderUserAdapter(private val cartItemList: List<CartItem>):
    RecyclerView.Adapter<OrderUserAdapter.OrderViewHolder>(){

   inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.itemName)
        val totalItemPrice: TextView = view.findViewById(R.id.totalItemPrice)
        val quantity: TextView = view.findViewById((R.id.itemQty))
        val subtractButton: AppCompatButton = view.findViewById(R.id.subtractButton)
        val addButton: AppCompatButton = view.findViewById(R.id.addButton)
        val image: ImageView = view.findViewById(R.id.itemImg)
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

        var totalItemPrice = cartList.quantity * item.price
        val trimmedTotalPrice = (totalItemPrice * 100).toInt() / 100.0

        holder.totalItemPrice.text = "$${trimmedTotalPrice}"
        holder.quantity.text = cartList.quantity.toString()

        val productImgView: ImageView = holder.image

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null;

        // TODO: make imageUrl dynamic
        executor.execute{
//            val imageUrl = "https://plus.unsplash.com/premium_photo-1690440686714-c06a56a1511c?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            val imageUrl = if(item.image.isNotEmpty()) item.image else "https://plus.unsplash.com/premium_photo-1690440686714-c06a56a1511c?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"

            try {
                val `in` = java.net.URL(imageUrl).openStream()

                image = BitmapFactory.decodeStream(`in`)

                handler.post{
                    productImgView.setImageBitmap(image)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.subtractButton.setOnClickListener {
            if (cartList.quantity > 0) { // Ensure quantity doesn't go below 0
                cartList.quantity--
                holder.quantity.text = cartList.quantity.toString()
                notifyItemChanged(position)  // Notify that this item has changed
            }
        }

        holder.addButton.setOnClickListener {
            cartList.quantity++
            holder.quantity.text = cartList.quantity.toString()
            notifyItemChanged(position)  // Notify that this item has changed

        }



    }


    override fun getItemCount(): Int {
        return cartItemList.size
    }

    fun getItemsWithQuantityGreaterThanZero(): List<CartItem> {
        return cartItemList.filter { it.quantity > 0 }
    }
}