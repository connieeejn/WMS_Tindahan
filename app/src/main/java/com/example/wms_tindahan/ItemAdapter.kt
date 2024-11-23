package com.example.wms_tindahan

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ItemAdapter(
    private val products: List<Item>
):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // TODO: add image
    // val image: ImageView = view.findViewById(R.id.itemImage)
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.itemName)
        val description: TextView = view.findViewById(R.id.itemDescription)
        val qty: TextView = view.findViewById(R.id.itemQty)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val category: TextView = view.findViewById(R.id.itemCategory)
        val itemCard: LinearLayout = view.findViewById(R.id.item_card)
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

        holder.itemCard.setOnClickListener {
            val intent = Intent(holder.itemView.context, Product::class.java)

            // Pass the product data to the Product activity
            intent.putExtra("product_name", product.item_name)
            intent.putExtra("product_description", product.item_description)
            intent.putExtra("product_price", product.price)
            intent.putExtra("product_qty", product.stock_quantity)
            intent.putExtra("product_category", product.category)

            // start activity
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

}