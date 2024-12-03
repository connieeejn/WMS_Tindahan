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

        // load image
        val productImgView: ImageView = holder.image
        loadImage(productImgView, item.image)

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