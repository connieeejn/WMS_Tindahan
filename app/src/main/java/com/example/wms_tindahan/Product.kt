package com.example.wms_tindahan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors



class Product : AppCompatActivity() {

    private lateinit var repository: ItemRepository
    private lateinit var nameTxtView: TextView
    private lateinit var descTxtView: TextView
    private lateinit var categoryTxtView: TextView
    private lateinit var priceTxtView: TextView
    private lateinit var qtyTxtView: TextView
    private lateinit var imgView: ImageView

    private var productId: Int = 0
    private var productName: String? = ""
    private var productDescription: String? = ""
    private var productPrice: Double = 0.0
    private var productQty: Int = 0
    private var productCategory: String? = ""
    private var productImgUrl: String? = ""

    private var userID: Int = 0
    private var updatedItemID: Int? = 0

    private lateinit var editProductLauncher: ActivityResultLauncher<Intent>
    private var updatedResultIntent: Intent? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)

        repository = ItemRepository(this)


        // initialize views
        nameTxtView = findViewById(R.id.productName)
        descTxtView = findViewById(R.id.productDescription)
        categoryTxtView = findViewById(R.id.productCategory)
        priceTxtView = findViewById(R.id.productPrice)
        qtyTxtView = findViewById(R.id.productQty)
        imgView = findViewById(R.id.productImg)


        // get the intent values and load the items
        loadDetails()


        // get userID getSharedPreferences
        val prefs = this.getSharedPreferences("userID", Context.MODE_PRIVATE)
        userID = prefs.getInt("userID", 0)

        Log.d("PREF", "${userID}")
        Log.d("ITEM_DETAILS", "ID: ${productId}, Name: ${productName}, Description: ${productDescription}, Price: $${productPrice}, Quantity: ${productQty}, Category: ${productCategory}")

        // load the result data when update is finish
        editProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK) {
                // Get updated data from the result intent
                updatedItemID = result.data?.getIntExtra("product_id", 0)
                val updatedProductName = result.data?.getStringExtra("product_name")
                val updatedProductDescription = result.data?.getStringExtra("product_description")
                val updatedProductPrice = result.data?.getDoubleExtra("product_price", 0.0)
                val updatedProductQty = result.data?.getIntExtra("product_qty", 0)
                val updatedProductCategory = result.data?.getStringExtra("product_category")
                val updatedProductImgUrl = result.data?.getStringExtra("product_image")

                // Update UI with the new values
                nameTxtView.text = updatedProductName
                descTxtView.text = updatedProductDescription
                priceTxtView.text = "$${updatedProductPrice.toString()}"
                qtyTxtView.text = "In stock: ${updatedProductQty.toString()}"
                categoryTxtView.text = updatedProductCategory

                // load image
                if (updatedProductImgUrl != null) {
                    loadImage(updatedProductImgUrl)
                }

                // store the edit activity result to be passed in inventory fragment
                updatedResultIntent = Intent()
                updatedResultIntent!!.putExtra("product_id", updatedItemID)
            }

        }

        // handle close, edit and delete
        val editBtn: Button = findViewById(R.id.updateProductBtn)
        val deleteBtn: Button = findViewById(R.id.deleteProductBtn)
        val closeBtn = findViewById<ImageButton>(R.id.prodCloseBtn)

        closeBtn.setOnClickListener {
            // go back to inventory page
            updatedResultIntent?.let {
                setResult(Activity.RESULT_OK, updatedResultIntent)
            }

            finish()
        }

        editBtn.setOnClickListener {
            val intent = Intent(this, AddNewProduct::class.java)

            // pass the data to product form activity
            intent.putExtra("product_id", productId)
            intent.putExtra("product_name", productName)
            intent.putExtra("product_description", productDescription)
            intent.putExtra("product_price", productPrice)
            intent.putExtra("product_qty", productQty)
            intent.putExtra("product_category", productCategory)
            intent.putExtra("product_image", productImgUrl)

            // Start the edit activity and wait for a result
            editProductLauncher.launch(intent)
        }

        deleteBtn.setOnClickListener {
            if (productId != 0) {
                repository.deleteItem(productId, userID,
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

    private fun loadDetails() {
        // get intent values
        productId = intent.getIntExtra("product_id", 0)
        productName = intent.getStringExtra("product_name")
        productDescription = intent.getStringExtra("product_description")
        productPrice = intent.getDoubleExtra("product_price", 0.0)
        productQty = intent.getIntExtra("product_qty", 0)
        productCategory = intent.getStringExtra("product_category")
        productImgUrl = intent.getStringExtra("product_image")


        // populate the views with the data
        Log.d("PRODUCT_ID", "Product ID: $productId");
        nameTxtView.text = productName
        descTxtView.text = productDescription
        priceTxtView.text = "$${productPrice.toString()}"
        qtyTxtView.text = "In stock: ${productQty.toString()}"
        categoryTxtView.text = productCategory


        // load image
        productImgUrl?.let { loadImage(it) }

    }

    private fun loadImage(productImgUrl: String) {
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