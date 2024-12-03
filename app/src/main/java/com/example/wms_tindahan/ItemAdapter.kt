package com.example.wms_tindahan

import android.annotation.SuppressLint
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
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.util.concurrent.Executors


class ItemAdapter(
    private var products: List<Item>,
    private val resultLauncher: ActivityResultLauncher<Intent>,
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

        // load image
        val productImgView: ImageView = holder.image
        loadImage(productImgView, product.image)

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

            // launch activity
            resultLauncher.launch(intent)
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateData(newItems: List<Item>) {
        this.products = newItems
        notifyDataSetChanged()
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