package com.example.wms_tindahan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Product : AppCompatActivity() {

    private lateinit var repository: ItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)

        repository = ItemRepository(this)

        val productId = intent.getIntExtra("product_id", 0)
        val productName = intent.getStringExtra("product_name")
        val productDescription = intent.getStringExtra("product_description")
        val productPrice = intent.getDoubleExtra("product_price", 0.0)
        val productQty = intent.getIntExtra("product_qty", 0)
        val productCategory = intent.getStringExtra("product_category")



        Log.d("PRODUCT_ID", "Product ID: $productId");
        findViewById<TextView>(R.id.productName).text = productName
        findViewById<TextView>(R.id.productDescription).text = productDescription
        findViewById<TextView>(R.id.productPrice).text = "$${productPrice.toString()}"
        findViewById<TextView>(R.id.productQty).text = "In stock: ${productQty.toString()}"
        findViewById<TextView>(R.id.productCategory).text = productCategory


        // handle close
        val closeBtn = findViewById<ImageButton>(R.id.prodCloseBtn)
        closeBtn.setOnClickListener {
            finish()
        }



        // handle edit and delete
        val editBtn: Button = findViewById(R.id.updateProductBtn)
        val deleteBtn: Button = findViewById(R.id.deleteProductBtn)

        // TODO: populate input data when edit
        // TODO: handle back button; always go back to the previous page
        editBtn.setOnClickListener {
            val intent = Intent(this, AddNewProduct::class.java)

            // pass the data to product form activity
            intent.putExtra("product_id", productId)
            intent.putExtra("product_name", productName)
            intent.putExtra("product_description", productDescription)
            intent.putExtra("product_price", productPrice)
            intent.putExtra("product_qty", productQty)
            intent.putExtra("product_category", productCategory)
            // TODO: add image

            startActivity(intent)
        }

        deleteBtn.setOnClickListener {

          //  val userId = getUserIdFromPreferences()  // Replace with actual method to get user_id
            // TODO: update userID
            val userId = 2;

            if (productId != 0) {
                repository.deleteItem(productId, userId,
                    onSuccess = {
                        Toast.makeText(this, "Item deleted successfully!", Toast.LENGTH_LONG).show()
                        finish()  // Go back to the main activity
                    },
                    onError = {
                        Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                    })
            } else {
                Toast.makeText(this, "Invalid Product ID", Toast.LENGTH_LONG).show()
            }
        }



    }
}