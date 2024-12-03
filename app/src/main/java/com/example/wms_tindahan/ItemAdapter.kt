package com.example.wms_tindahan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.wms_tindahan.fragment.InventoryFragment.Companion.REQUEST_CODE
import java.lang.Exception
import java.util.concurrent.Executors


class ItemAdapter(
    private var products: List<Item>,
    private val fragment: Fragment,

    ):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.itemName)
        val description: TextView = view.findViewById(R.id.itemDescription)
        val qty: TextView = view.findViewById(R.id.itemQty)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val category: TextView = view.findViewById(R.id.itemCategory)
        val itemCard: LinearLayout = view.findViewById(R.id.item_card)
        val image: ImageView = view.findViewById(R.id.itemImg)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ItemViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val product = products[position]

        holder.name.text = product.item_name
        holder.description.text = product.item_description
        holder.price.text = "$${product.price.toString()}"
        holder.qty.text = "In stock: ${product.stock_quantity.toString()}"
        holder.category.text = product.category
        // TODO: add image

        // handle image
        val productImgView: ImageView = holder.image

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())

        var image: Bitmap? = null;

        // TODO: make imageUrl dynamic
        executor.execute{
            val imageUrl = if(product.image.isNotEmpty()) product.image else "https://plus.unsplash.com/premium_photo-1690440686714-c06a56a1511c?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"

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

        holder.itemCard.setOnClickListener {
            val intent = Intent(holder.itemView.context, Product::class.java)

            // Pass the product data to the Product activity
            intent.putExtra("product_id", product.id)
            intent.putExtra("product_name", product.item_name)
            intent.putExtra("product_description", product.item_description)
            intent.putExtra("product_price", product.price)
            intent.putExtra("product_qty", product.stock_quantity)
            intent.putExtra("product_category", product.category)
            intent.putExtra("product_image", product.image)

            // start activity
            holder.itemView.context.startActivity(intent)

            // Start ProductActivity and expect a result
            fragment.startActivityForResult(intent, REQUEST_CODE)
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateData(newItems: List<Item>) {
        this.products = newItems
        notifyDataSetChanged()
    }

}